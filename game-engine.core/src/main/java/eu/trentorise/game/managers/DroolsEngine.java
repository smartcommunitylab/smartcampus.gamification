/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.LogManager;
import org.drools.core.io.impl.ByteArrayResource;
import org.drools.verifier.Verifier;
import org.drools.verifier.VerifierError;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.command.Command;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.command.CommandFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.LoggingRuleListener;
import eu.trentorise.game.model.Action;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.InputData;
import eu.trentorise.game.model.Member;
import eu.trentorise.game.model.Player;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.Propagation;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.UpdateMembers;
import eu.trentorise.game.model.UpdateTeams;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.FSRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.model.core.UrlRule;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

@Component
public class DroolsEngine implements GameEngine {

	private final Logger logger = LoggerFactory.getLogger(DroolsEngine.class);

	@Autowired
	private NotificationManager notificationSrv;

	@Autowired
	private GameService gameSrv;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private Workflow workflow;

	private KieServices kieServices = KieServices.Factory.get();

	public PlayerState execute(String gameId, PlayerState state, String action,
			Map<String, Object> data, List<Object> factObjects) {

		StopWatch stopWatch = LogManager.getLogger(
				StopWatch.DEFAULT_LOGGER_NAME).getAppender("perf-file") != null ? new Log4JStopWatch()
				: null;
		if (stopWatch != null) {
			stopWatch.start("game execution");
		}

		Game game = gameSrv.loadGameDefinitionById(gameId);
		if (game != null && game.isTerminated()) {
			throw new IllegalArgumentException(String.format(
					"game %s is expired", gameId));
		}

		loadGameRules(gameId);

		KieContainer kieContainer = kieServices.newKieContainer(kieServices
				.getRepository().getDefaultReleaseId());

		StatelessKieSession kSession = kieContainer.newStatelessKieSession();
		kSession.addEventListener(new LoggingRuleListener(state.getPlayerId()));

		List<Command> cmds = new ArrayList<Command>();

		if (data != null) {
			cmds.add(CommandFactory.newInsert(new InputData(data)));
		}
		if (!StringUtils.isBlank(action)) {
			cmds.add(CommandFactory.newInsert(new Action(action)));
		}

		if (factObjects != null) {
			cmds.add(CommandFactory.newInsertElements(factObjects));
		}

		cmds.add(CommandFactory.newInsert(new Game(gameId)));

		cmds.add(CommandFactory.newInsert(new Player(state.getPlayerId(),
				state instanceof TeamState)));

		// filter state removing all ended or completed challenges for the
		// player
		Set<GameConcept> filteredByChallenge = new HashSet<>(state.getState());
		Set<GameConcept> archivedChallenges = new HashSet<>();
		Date now = new Date();
		for (Iterator<GameConcept> iter = filteredByChallenge.iterator(); iter
				.hasNext();) {
			GameConcept gc = iter.next();
			if (gc instanceof ChallengeConcept) {
				ChallengeConcept challenge = (ChallengeConcept) gc;
				if (challenge.isCompleted()
						|| (challenge.getStart() != null && challenge
								.getStart().after(now))
						|| (challenge.getEnd() != null && challenge.getEnd()
								.before(now))) {
					iter.remove();
					archivedChallenges.add(gc);
				}
			}

		}

		// ATTENTION: Drools modifies objects inserted in working memory by
		// reference

		cmds.add(CommandFactory.newInsertElements(filteredByChallenge));
		cmds.add(CommandFactory.newInsert(state.getCustomData()));
		cmds.add(CommandFactory.newFireAllRules());

		// queries
		cmds.add(CommandFactory.newQuery("retrieveState", "getGameConcepts"));
		cmds.add(CommandFactory.newQuery("retrieveNotifications",
				"getNotifications"));
		cmds.add(CommandFactory.newQuery("retrieveCustomData", "getCustomData"));
		cmds.add(CommandFactory.newQuery("retrieveUpdateTeams",
				"getUpdateTeams"));
		cmds.add(CommandFactory.newQuery("retrieveUpdateMembers",
				"getUpdateMembers"));
		cmds.add(CommandFactory.newQuery("retrieveLevel", "getLevel"));
		cmds.add(CommandFactory.newQuery("retrieveMember", "getMember"));

		kSession = loadGameConstants(kSession, gameId);

		ExecutionResults results = kSession.execute(CommandFactory
				.newBatchExecution(cmds));

		// new state contains archived challenges and all GameConcept
		// loaded in engine session
		Set<GameConcept> newState = new HashSet<GameConcept>(archivedChallenges);

		Iterator<QueryResultsRow> iter = ((QueryResults) results
				.getValue("retrieveState")).iterator();
		while (iter.hasNext()) {
			newState.add((GameConcept) iter.next().get("$result"));
		}

		List<CustomData> customData = new ArrayList<CustomData>();

		iter = ((QueryResults) results.getValue("retrieveCustomData"))
				.iterator();
		while (iter.hasNext()) {
			CustomData stateCustomData = (CustomData) iter.next().get("$data");
			customData.add(stateCustomData);
		}

		iter = ((QueryResults) results.getValue("retrieveNotifications"))
				.iterator();
		while (iter.hasNext()) {
			Notification note = (Notification) iter.next()
					.get("$notifications");
			notificationSrv.notificate(note);
			logger.info("send notification: {}", note.toString());
		}

		iter = ((QueryResults) results.getValue("retrieveUpdateTeams"))
				.iterator();

		if (iter.hasNext()) {
			Set<Object> facts = new HashSet<>();
			Iterator<QueryResultsRow> iter1 = null;
			while (iter.hasNext()) {
				UpdateTeams updateCalls = (UpdateTeams) iter.next()
						.get("$data");
				iter1 = ((QueryResults) results.getValue("retrieveLevel"))
						.iterator();
				int level = 1;
				if (iter1.hasNext()) {
					level = (int) iter1.next().get("$data");
					level++;
				}
				facts.add(new Propagation(updateCalls.getPropagationAction(),
						level));
			}

			List<TeamState> playerTeams = playerSrv.readTeams(gameId,
					state.getPlayerId());
			logger.info("Player {} belongs to {} teams", state.getPlayerId(),
					playerTeams.size());
			if (playerTeams.size() > 0) {
				logger.info("call for update with data {}", data);
			}

			iter1 = ((QueryResults) results.getValue("retrieveMember"))
					.iterator();
			Member fromPropagation = null;
			if (iter1.hasNext()) {
				fromPropagation = (Member) iter1.next().get("$data");
			}
			facts.add(new Member(state.getPlayerId(),
					data == null ? (fromPropagation != null ? fromPropagation
							.getInputData() : null) : data));
			for (TeamState team : playerTeams) {
				workflow.apply(gameId, action, team.getPlayerId(), null,
						new ArrayList<>(facts));
			}
		}
		iter = ((QueryResults) results.getValue("retrieveUpdateMembers"))
				.iterator();
		if (iter.hasNext()) {
			Set<Object> facts = new HashSet<>();
			while (iter.hasNext()) {
				UpdateMembers updateCalls = (UpdateMembers) iter.next().get(
						"$data");
				facts.add(new Propagation(updateCalls.getPropagationAction()));
			}
			// check if a propagation to team members is needed
			try {
				TeamState team = playerSrv
						.readTeam(gameId, state.getPlayerId());
				List<String> members = team.getMembers();
				facts.add(new Team(state.getPlayerId(), data));
				logger.info("Team {} has {} members", state.getPlayerId(),
						members.size());
				for (String member : members) {
					workflow.apply(gameId, action, member, null,
							new ArrayList<>(facts));
				}
			} catch (ClassCastException e) {
				logger.info(String
						.format("%s is not a team, there is no propagation to team members",
								state.getPlayerId()));
			}

		}

		state.setState(newState);
		// fix for dataset prior than 0.9 version
		state.setCustomData(customData.isEmpty() ? new CustomData()
				: customData.get(0));

		if (stopWatch != null) {
			stopWatch.stop("game execution", String.format(
					"execution for game %s of player %s", gameId,
					state.getPlayerId()));
		}
		return state;
	}

	private StatelessKieSession loadGameConstants(StatelessKieSession kSession,
			String gameId) {

		// load game constants
		InputStream constantsFileStream = null;
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null && g.getRules() != null) {
			for (String ruleUrl : g.getRules()) {
				Rule r = gameSrv.loadRule(gameId, ruleUrl);
				if ((r != null && r.getName() != null && r.getName().equals(
						"constants"))
						|| r instanceof UrlRule
						&& ((UrlRule) r).getUrl().contains("constants")) {
					try {
						constantsFileStream = r.getInputStream();
					} catch (IOException e) {
						logger.error("Exception loading constants file", e);
					}
				}
			}
		}

		if (constantsFileStream != null) {
			try {
				PropertiesConfiguration constants = new PropertiesConfiguration();
				constants.load(constantsFileStream);
				constants.setListDelimiter(',');
				logger.debug("constants file loaded for game {}", gameId);
				Iterator<String> constantsIter = constants.getKeys();
				while (constantsIter.hasNext()) {
					String constant = constantsIter.next();
					kSession.setGlobal(constant,
							numberConversion(constants.getProperty(constant)));
					logger.debug("constant {} loaded", constant);
				}
			} catch (ConfigurationException e) {
				logger.error("constants loading exception");
			}
		} else {
			logger.info("Rule constants file not found");
		}
		return kSession;
	}

	private Object numberConversion(Object value) {

		if (value instanceof String) {
			String converted = (String) value;
			if (NumberUtils.isNumber(converted)
					&& converted.toLowerCase().contains("l")) {
				return new Long(converted.substring(0, converted.length() - 1));
			}
			if (NumberUtils.isNumber(converted) && !converted.contains(".")) {
				return new Integer(converted);
			}
			if (NumberUtils.isNumber(converted) && converted.contains(".")) {
				return new Double(converted);
			}
		}

		return value;
	}

	private void loadGameRules(String gameId) {
		KieFileSystem kfs = kieServices.newKieFileSystem();
		RuleLoader ruleLoader = new RuleLoader(gameId);
		// load core.drl

		Resource coreRes;
		try {
			coreRes = ruleLoader.load("classpath://rules/core.drl");
			kfs.write(coreRes);
			logger.info("Core rules loaded");
		} catch (MalformedURLException e) {
			logger.error("Exception loading core.drl");
		}

		// load rules

		Game game = gameSrv.loadGameDefinitionById(gameId);

		if (game != null && game.getRules() != null) {
			for (String rule : game.getRules()) {
				Resource r1;
				try {
					r1 = ruleLoader.load(rule);
					// fix to not load constant file
					if (r1 != null) {
						kfs.write(r1);
						logger.debug("{} loaded", rule);
					}
				} catch (MalformedURLException e) {
					logger.error(
							"Malformed URL loading rule {}, rule not loaded",
							rule);
				} catch (RuntimeException e) {
					logger.error("Exception loading rule {}", rule);
				}
			}
		}
		kieServices.newKieBuilder(kfs).buildAll();
		logger.info("Rules repository built");

	}

	private class RuleLoader {
		private String gameId;

		public RuleLoader(String gameId) {
			this.gameId = gameId;
		}

		public boolean isConstantsRule(String ruleUrl) {
			boolean classpathCheck = ruleUrl
					.startsWith(ClasspathRule.URL_PROTOCOL)
					&& ruleUrl.contains("/constants");
			boolean fsCheck = ruleUrl.startsWith(FSRule.URL_PROTOCOL)
					&& ruleUrl.contains("/constants");
			boolean dbCheck = ruleUrl.startsWith(DBRule.URL_PROTOCOL);
			if (dbCheck) {
				Rule r = gameSrv.loadRule(gameId, ruleUrl);
				dbCheck = r != null && r.getName() != null
						&& r.getName().equals("constants");
			}

			return classpathCheck || fsCheck || dbCheck;
		}

		public Resource load(String ruleUrl) throws MalformedURLException {
			Resource res = null;
			String url = null;
			if (isConstantsRule(ruleUrl)) {
				return null;
			}
			if (ruleUrl.startsWith(ClasspathRule.URL_PROTOCOL)) {
				url = ruleUrl.substring(ClasspathRule.URL_PROTOCOL.length());
				res = kieServices.getResources().newClassPathResource(url);
			} else if (ruleUrl.startsWith(FSRule.URL_PROTOCOL)) {
				url = ruleUrl.substring(FSRule.URL_PROTOCOL.length());
				res = kieServices.getResources().newFileSystemResource(url);
			} else if (ruleUrl.startsWith(DBRule.URL_PROTOCOL)) {
				Rule r = gameSrv.loadRule(gameId, ruleUrl);
				if (r != null) {
					res = kieServices.getResources().newReaderResource(
							new StringReader(((DBRule) r).getContent()));
					res.setSourcePath("rules/" + r.getGameId() + "/"
							+ ((DBRule) r).getId() + ".drl");
				} else {
					logger.error("DBRule {} not exist", url);
				}
			} else {
				throw new MalformedURLException("resource URL not supported");
			}
			return res;
		}
	}

	@Override
	public List<String> validateRule(String content) {
		List<String> result = new ArrayList<String>();
		if (content != null) {
			VerifierBuilder vBuilder = VerifierBuilderFactory
					.newVerifierBuilder();
			// Check that the builder works.
			if (!vBuilder.hasErrors()) {
				Verifier verifier = vBuilder.newVerifier();
				verifier.addResourcesToVerify(
						new ByteArrayResource(content.getBytes())
								.setTargetPath("/t.drl"), ResourceType.DRL);
				for (VerifierError err : verifier.getErrors()) {
					result.add(err.getMessage());
				}
			} else {
				logger.error("Drools verifier instantiation exception");
			}
		}
		return result;
	}
}
