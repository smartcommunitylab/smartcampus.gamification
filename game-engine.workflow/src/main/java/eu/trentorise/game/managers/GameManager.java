package eu.trentorise.game.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GameRepo;
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

	@PostConstruct
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

			game.getRules().add("finalClassificationBadges.drl");
			game.getRules().add("greenBadges.drl");
			game.getRules().add("greenPoints.drl");
			game.getRules().add("healthBadges.drl");
			game.getRules().add("healthPoints.drl");
			game.getRules().add("initState.drl");
			game.getRules().add("prBadges.drl");
			game.getRules().add("prPoints.drl");
			game.getRules().add("specialBadges.drl");
			game.getRules().add("weekClassificationBadges.drl");

			game.setTasks(new HashSet<GameTask>());

			/**
			 * 
			 * ONLINE GAME TASK
			 */

			game.getTasks().addAll(finalGameTasks());
			game.getTasks().addAll(testTasks());

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
		schedule.setCronExpression("0 0 19 17 * *");
		ClassificationTask task4 = new ClassificationTask(schedule, 1,
				"green leaves", "week classification green");
		tasks.add(task4);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 5 19 17 * *");
		ClassificationTask task5 = new ClassificationTask(schedule, 1,
				"health", "week classification health");
		tasks.add(task5);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 10 19 17 * *");
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
		schedule.setCronExpression("0 0 0 8 * *");
		ClassificationTask task1 = new ClassificationTask(schedule, 3,
				"green leaves", "final classification green");
		tasks.add(task1);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 5 0 8 * *");
		ClassificationTask task2 = new ClassificationTask(schedule, 3,
				"health", "final classification health");
		tasks.add(task2);

		schedule = new TaskSchedule();
		schedule.setCronExpression("0 10 0 8 * *");
		ClassificationTask task3 = new ClassificationTask(schedule, 3, "p+r",
				"final classification p+r");
		tasks.add(task3);

		return tasks;

	}

	public String getGameIdByAction(String actionId) {
		return repo.get(actionId);
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
}
