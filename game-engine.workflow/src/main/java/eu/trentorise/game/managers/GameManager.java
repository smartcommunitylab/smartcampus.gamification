package eu.trentorise.game.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.FSRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Rule;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GameRepo;
import eu.trentorise.game.repo.RuleRepo;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.task.ClassificationTask;

@Component
public class GameManager implements GameService {

	private final Logger logger = LoggerFactory.getLogger(GameManager.class);

	private static Map<String, String> repo;

	@Autowired
	@Qualifier("quartzTaskManager")
	// @Qualifier("taskManager")
	TaskService taskManager;

	@Autowired
	AppContextProvider provider;

	@Autowired
	GameRepo gameRepo;

	@Autowired
	RuleRepo ruleRepo;

	// @PostConstruct
	@SuppressWarnings("unused")
	private void initRepo() {
		repo = new HashMap<String, String>();
		repo.put("save_itinerary", "game1");
		repo.put("classification", "game1");

		String gameId = "game1";
		// init game1 for dev purpose
		if (loadGameDefinitionById(gameId) == null) {
			Game game = new Game();
			game.setId(gameId);
			game.setName("rovereto explorer");

			game.setActions(new HashSet<String>());
			game.getActions().add("save_itinerary");
			game.getActions().add("classification");

			game.setRules(new HashSet<String>());

			game.getRules().add(
					"classpath://rules/" + gameId
							+ "/finalClassificationBadges.drl");
			game.getRules().add(
					"classpath://rules/" + gameId + "/greenBadges.drl");
			game.getRules().add(
					"classpath://rules/" + gameId + "/greenPoints.drl");
			game.getRules().add(
					"classpath://rules/" + gameId + "/healthBadges.drl");
			game.getRules().add(
					"classpath://rules/" + gameId + "/healthPoints.drl");
			game.getRules().add(
					"classpath://rules/" + gameId + "/initState.drl");
			game.getRules()
					.add("classpath://rules/" + gameId + "/prBadges.drl");
			game.getRules()
					.add("classpath://rules/" + gameId + "/prPoints.drl");
			game.getRules().add(
					"classpath://rules/" + gameId + "/specialBadges.drl");
			game.getRules().add(
					"classpath://rules/" + gameId
							+ "/weekClassificationBadges.drl");

			game.setTasks(new HashSet<GameTask>());

			/**
			 * 
			 * ONLINE GAME TASK
			 */

			game.getTasks().addAll(finalGameTasks());
			// game.getTasks().addAll(testTasks());

			saveGameDefinition(game);
			logger.debug("created game {}", gameId);
		} else {
			logger.debug("found game {}", gameId);
		}
	}

	private Set<GameTask> testTasks() {
		Set<GameTask> tasks = new HashSet<GameTask>();

		// week classification

		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("0 0 15 18 * *");
		ClassificationTask task4 = new ClassificationTask(schedule, 1,
				"green leaves", "week classification green");
		tasks.add(task4);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 5 15 18 * *");
		ClassificationTask task5 = new ClassificationTask(schedule, 1,
				"health", "week classification health");
		tasks.add(task5);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 10 15 18 * *");
		ClassificationTask task6 = new ClassificationTask(schedule, 1, "p+r",
				"week classification p+r");
		tasks.add(task6);

		// final classification

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 0 12 19 * *");
		ClassificationTask task1 = new ClassificationTask(schedule, 3,
				"green leaves", "final classification green");
		tasks.add(task1);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 5 12 19 * *");
		ClassificationTask task2 = new ClassificationTask(schedule, 3,
				"health", "final classification health");
		tasks.add(task2);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 10 12 19 * *");
		ClassificationTask task3 = new ClassificationTask(schedule, 3, "p+r",
				"final classification p+r");
		tasks.add(task3);

		return tasks;
	}

	private Set<GameTask> finalGameTasks() {
		Set<GameTask> tasks = new HashSet<GameTask>();

		// week classification

		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("0 0 0 1 * *");
		ClassificationTask task4 = new ClassificationTask(schedule, 1,
				"green leaves", "week classification green");
		tasks.add(task4);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 5 0 1 * *");
		ClassificationTask task5 = new ClassificationTask(schedule, 1,
				"health", "week classification health");
		tasks.add(task5);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 10 0 1 * *");
		ClassificationTask task6 = new ClassificationTask(schedule, 1, "p+r",
				"week classification p+r");
		tasks.add(task6);

		// final classification

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 10 0 9 * *");
		ClassificationTask task1 = new ClassificationTask(schedule, 3,
				"green leaves", "final classification green");
		tasks.add(task1);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 15 0 9 * *");
		ClassificationTask task2 = new ClassificationTask(schedule, 3,
				"health", "final classification health");
		tasks.add(task2);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 20 0 9 * *");
		ClassificationTask task3 = new ClassificationTask(schedule, 3, "p+r",
				"final classification p+r");
		tasks.add(task3);

		return tasks;

	}

	public String getGameIdByAction(String actionId) {
		GamePersistence game = gameRepo.findByActions(actionId);
		return game != null ? game.getId() : null;
	}

	public void startupTasks(String gameId) {
		Game game = loadGameDefinitionById(gameId);
		if (game != null) {
			for (GameTask task : game.getTasks()) {
				taskManager.createTask(task, (GameContext) provider
						.getApplicationContext().getBean("gameCtx", gameId));
			}
		}

	}

	public void saveGameDefinition(Game game) {
		gameRepo.save(new GamePersistence(game));
	}

	public Game loadGameDefinitionById(String gameId) {
		GamePersistence gp = gameRepo.findOne(gameId);
		return gp == null ? null : gp.toGame();
	}

	public List<Game> loadGames() {
		List<Game> result = new ArrayList<Game>();
		for (GamePersistence gp : gameRepo.findAll()) {
			result.add(gp.toGame());
		}
		return result;
	}

	public void addRule(Rule rule) {
		if (rule != null) {
			Game game = loadGameDefinitionById(rule.getGameId());
			if (game != null) {
				if (rule instanceof ClasspathRule) {
					game.getRules().add(
							"classpath://" + ((ClasspathRule) rule).getUrl());
				}

				if (rule instanceof FSRule) {
					game.getRules().add("file://" + ((FSRule) rule).getUrl());
				}

				if (rule instanceof DBRule) {
					rule = ruleRepo.save((DBRule) rule);
					game.getRules().add("db://" + ((DBRule) rule).getId());
				}

				saveGameDefinition(game);
			} else {
				logger.error("Game {} not found", rule.getGameId());
			}
		}
	}

	public Rule loadRule(String gameId, String url) {
		Rule rule = null;
		if (url != null) {
			if (url.startsWith("db://")) {
				url = url.substring("db://".length());
				return ruleRepo.findOne(url);
			} else if (url.startsWith("classpath://")) {
				url = url.substring("classpath://".length());
				if (Thread.currentThread().getContextClassLoader()
						.getResource(url) != null) {
					return new ClasspathRule(gameId, url);
				}

			} else if (url.startsWith("file://")) {
				url = url.substring("file://".length());
				if (new File(url).exists()) {
					return new FSRule(gameId, url);
				}
			}
		}
		return rule;
	}

}
