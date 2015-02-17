package eu.trentorise.game.managers;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.command.Command;
import org.kie.api.io.Resource;
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
import eu.trentorise.game.model.DBRule;
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
			Map<String, Object> data) {

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

		cmds.add(CommandFactory.newInsert(new Game(gameId)));
		cmds.add(CommandFactory.newInsert(new Player(state.getPlayerId())));

		cmds.add(CommandFactory.newInsertElements(state.getState()));
		cmds.add(CommandFactory.newFireAllRules());
		cmds.add(CommandFactory.newQuery("retrieveState", "getGameConcepts"));
		cmds.add(CommandFactory.newQuery("retrieveNotifications",
				"getNotifications"));

		kSession = loadGameConstants(kSession, gameId);

		ExecutionResults results = kSession.execute(CommandFactory
				.newBatchExecution(cmds));

		Set<GameConcept> newState = new HashSet<GameConcept>();

		Iterator<QueryResultsRow> iter = ((QueryResults) results
				.getValue("retrieveState")).iterator();
		while (iter.hasNext()) {
			newState.add((GameConcept) iter.next().get("$result"));
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
		return state;
	}

	private StatelessKieSession loadGameConstants(StatelessKieSession kSession,
			String gameId) {
		// load game constants

		URL constantsFileURL = Thread.currentThread().getContextClassLoader()
				.getResource("rules/" + gameId + "/constants");
		if (constantsFileURL != null) {
			try {
				PropertiesConfiguration constants = new PropertiesConfiguration(
						constantsFileURL);
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
				logger.error("{} loading exception", constantsFileURL);
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

		for (String rule : game.getRules()) {
			Resource r1;
			try {
				r1 = ruleLoader.load(rule);
				kfs.write(r1);
				logger.debug("{} loaded", rule);
			} catch (MalformedURLException e) {
				logger.error("Malformed URL loading rule {}, rule not loaded",
						rule);
			} catch (RuntimeException e) {
				logger.error("Exception loading rule {}", rule);
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

		public Resource load(String ruleUrl) throws MalformedURLException {
			Resource res = null;
			String url = null;
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

}
