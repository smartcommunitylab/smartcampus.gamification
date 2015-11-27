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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.Action;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.FSRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.InputData;
import eu.trentorise.game.model.Notification;
import eu.trentorise.game.model.Player;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.Rule;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;

@Component
public class DroolsEngine implements GameEngine {

	private final Logger logger = LoggerFactory.getLogger(DroolsEngine.class);

	@Autowired
	NotificationManager notificationSrv;

	@Autowired
	GameService gameSrv;

	private KieServices kieServices = KieServices.Factory.get();

	public PlayerState execute(String gameId, PlayerState state, String action,
			Map<String, Object> data, List<Object> factObjects) {

		Game game = gameSrv.loadGameDefinitionById(gameId);
		if (game != null && game.isTerminated()) {
			throw new IllegalArgumentException(String.format(
					"game %s is expired", gameId));
		}

		loadGameRules(gameId);

		KieContainer kieContainer = kieServices.newKieContainer(kieServices
				.getRepository().getDefaultReleaseId());

		StatelessKieSession kSession = kieContainer.newStatelessKieSession();

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
		cmds.add(CommandFactory.newInsert(new Player(state.getPlayerId())));

		cmds.add(CommandFactory.newInsertElements(state.getState()));
		cmds.add(CommandFactory.newInsert(state.getCustomData()));
		cmds.add(CommandFactory.newFireAllRules());
		cmds.add(CommandFactory.newQuery("retrieveState", "getGameConcepts"));
		cmds.add(CommandFactory.newQuery("retrieveNotifications",
				"getNotifications"));
		cmds.add(CommandFactory.newQuery("retrieveCustomData", "getCustomData"));

		kSession = loadGameConstants(kSession, gameId);

		ExecutionResults results = kSession.execute(CommandFactory
				.newBatchExecution(cmds));

		Set<GameConcept> newState = new HashSet<GameConcept>();

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

		state.setState(newState);
		// fix for previous dataset versions
		state.setCustomData(customData.isEmpty() ? new CustomData()
				: customData.get(0));
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
				if (r != null && r.getName() != null
						&& r.getName().equals("constants")) {
					if (r instanceof DBRule) {
						constantsFileStream = new ByteArrayInputStream(
								((DBRule) r).getContent().getBytes());
					}
				}
				if (r instanceof ClasspathRule
						&& ((ClasspathRule) r).getUrl().contains("constants")) {
					String url = ((ClasspathRule) r).getUrl();
					url = url.replace("classpath://", "");
					constantsFileStream = Thread.currentThread()
							.getContextClassLoader().getResourceAsStream(url);
				}

				if (r instanceof FSRule
						&& ((FSRule) r).getUrl().contains("constants")) {
					String url = ((FSRule) r).getUrl();
					url = url.replace("file://", "");
					try {
						constantsFileStream = new FileInputStream(url);
					} catch (FileNotFoundException e) {
						logger.error(String.format(
								"error opening constants %s of game %s", url,
								gameId));
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
			boolean classpathCheck = ruleUrl.startsWith("classpath://")
					&& ruleUrl.contains("/constants");
			boolean fsCheck = ruleUrl.startsWith("file://")
					&& ruleUrl.contains("/constants");
			boolean dbCheck = ruleUrl.startsWith("db://");
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
			if (ruleUrl.startsWith("classpath://")) {
				url = ruleUrl.substring("classpath://".length());
				res = kieServices.getResources().newClassPathResource(url);
			} else if (ruleUrl.startsWith("file://")) {
				url = ruleUrl.substring("file://".length());
				res = kieServices.getResources().newFileSystemResource(url);
			} else if (ruleUrl.startsWith("db://")) {
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
