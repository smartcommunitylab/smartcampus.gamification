package eu.trentorise.game.managers;

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
import org.kie.api.io.KieResources;
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
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.InputData;
import eu.trentorise.game.model.Player;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;

@Component
public class DroolsEngine implements GameEngine {

	private final Logger logger = LoggerFactory.getLogger(DroolsEngine.class);

	@Autowired
	NotificationManager notificationSrv;

	@Autowired
	GameService gameSrv;

	private RuleLoader ruleLoader = new RuleLoader();

	private KieServices kieServices = KieServices.Factory.get();

	public PlayerState execute(String gameId, PlayerState state, String action,
			Map<String, Object> data) {

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

		kSession = loadGameConstants(kSession, gameId);

		kSession.setGlobal("notificationSrv", notificationSrv);

		ExecutionResults results = kSession.execute(CommandFactory
				.newBatchExecution(cmds));

		Set<GameConcept> newState = new HashSet<GameConcept>();

		Iterator<QueryResultsRow> iter = ((QueryResults) results
				.getValue("retrieveState")).iterator();
		while (iter.hasNext()) {
			newState.add((GameConcept) iter.next().get("$result"));
		}

		state.setState(newState);
		return state;
	}

	private StatelessKieSession loadGameConstants(StatelessKieSession kSession,
			String gameId) {
		// load game constants

		URL costantsFileURL = Thread.currentThread().getContextClassLoader()
				.getResource("rules/" + gameId + "/constants");
		try {
			PropertiesConfiguration constants = new PropertiesConfiguration(
					costantsFileURL);
			constants.setListDelimiter(',');
			logger.info("constants file loaded for game {}", gameId);
			Iterator<String> constantsIter = constants.getKeys();
			while (constantsIter.hasNext()) {
				String constant = constantsIter.next();
				kSession.setGlobal(constant,
						numberConversion(constants.getProperty(constant)));
				logger.debug("constant {} loaded", constant);
			}
		} catch (ConfigurationException e) {
			logger.error("{} loading exception", costantsFileURL);
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
		KieResources res = kieServices.getResources();
		KieFileSystem kfs = kieServices.newKieFileSystem();

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
			// String path = "rules/" + gameId + "/" + rule;
			Resource r1;
			try {
				r1 = ruleLoader.load(rule);
				kfs.write(r1);
				logger.info("{} loaded", rule);
			} catch (MalformedURLException e) {
				logger.error("Malformed URL loading rule {}, rule not loaded",
						rule);
			}
		}
		kieServices.newKieBuilder(kfs).buildAll();
		logger.info("Rules repository built");

	}

	private class RuleLoader {
		public RuleLoader() {
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
				url = ruleUrl.substring("db://".length());
				// TODO LOAD from DB
			} else {
				throw new MalformedURLException("resource URL not supported");
			}
			return res;
		}
	}

}
