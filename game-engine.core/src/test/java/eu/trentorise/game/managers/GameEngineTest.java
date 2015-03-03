package eu.trentorise.game.managers;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.task.ClassificationTask;

/**
 * 
 * Actually execution tests use Thread.sleep to wait QueueGameWorkflow async
 * conclusion. This MUST to be fixed, because test result can be machine
 * dependent.
 * 
 * 
 * @author mirko perillo
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GameEngineTest {

	@Autowired
	private GameManager gameManager;

	@Autowired
	private GameEngine engine;

	@Autowired
	private MongoTemplate mongo;

	private static final String GAME = "coreGameTest";
	private static final String ACTION = "save_itinerary";
	private static final String PLAYER = "iansolo";

	private static final long WAIT_EXEC = 15 * 1000;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	private void initClasspathRuleGame() {
		mongo.getDb().dropDatabase();
		gameManager.saveGameDefinition(defineGame().toGame());

		// add rules

		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/initState.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/specialBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/weekClassificationBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/finalClassificationBadges.drl"));
	}

	private void initDBRuleGame() {
		mongo.getDb().dropDatabase();
		gameManager.saveGameDefinition(defineGame().toGame());

		// add rules
		try {

			String c = FileUtils
					.readFileToString(new File(Thread.currentThread()
							.getContextClassLoader()
							.getResource("rules/" + GAME + "/initState.drl")
							.getFile()));

			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/greenBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/greenPoints.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/healthPoints.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/healthBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/prPoints.drl").getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/prBadges.drl").getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/specialBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"rules/" + GAME + "/weekClassificationBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME, c));
			c = FileUtils.readFileToString(new File(Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"rules/" + GAME + "/finalClassificationBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME, c));

		} catch (IOException e) {
			Assert.fail("Fail to save rule in mongo from classpath");
		}
	}

	@Test
	public void loadGame() {
		initClasspathRuleGame();
		Assert.assertEquals(GAME, gameManager.getGameIdByAction(ACTION));
	}

	@Test
	public void execution() throws InterruptedException {
		initClasspathRuleGame();
		PlayerState p = new PlayerState(PLAYER, GAME);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		params.put("bikesharing", true);
		params.put("sustainable", true);
		params.put("p+r", true);
		params.put("park", "MANIFATTURA");
		p = engine.execute(GAME, p, ACTION, params);
		Thread.sleep(WAIT_EXEC);
		// expected 60 greenPoints and earned 10-point 50-point green badges
		boolean found = false;
		for (GameConcept gc : p.getState()) {
			if (gc instanceof PointConcept
					&& gc.getName().equals("green leaves")) {
				found = true;
				Assert.assertEquals(70d, ((PointConcept) gc).getScore()
						.doubleValue(), 0);
			}
			if (gc instanceof BadgeCollectionConcept
					&& gc.getName().equals("green leaves")) {
				found = found && true;
				Assert.assertArrayEquals(new String[] { "10-point-green",
						"50-point-green" }, ((BadgeCollectionConcept) gc)
						.getBadgeEarned().toArray(new String[1]));
			}

		}
		if (!found) {
			Assert.fail("gameconcepts not found");
		}
	}

	@Test
	public void mongoExecution() throws InterruptedException {
		initDBRuleGame();
		PlayerState p = new PlayerState(PLAYER, GAME);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		params.put("bikesharing", true);
		params.put("sustainable", true);
		params.put("p+r", true);
		params.put("park", "MANIFATTURA");
		p = engine.execute(GAME, p, ACTION, params);
		// expected 60 greenPoints and earned 10-point 50-point green badges
		Thread.sleep(WAIT_EXEC);
		boolean found = false;
		for (GameConcept gc : p.getState()) {
			if (gc instanceof PointConcept
					&& gc.getName().equals("green leaves")) {
				found = true;
				Assert.assertEquals(70d, ((PointConcept) gc).getScore()
						.doubleValue(), 0);
			}
			if (gc instanceof BadgeCollectionConcept
					&& gc.getName().equals("green leaves")) {
				found = found && true;
				Assert.assertArrayEquals(new String[] { "10-point-green",
						"50-point-green" }, ((BadgeCollectionConcept) gc)
						.getBadgeEarned().toArray(new String[1]));
			}

		}
		if (!found) {
			Assert.fail("gameconcepts not found");
		}
	}

	@Test
	public void mongoRule() {
		mongo.getDb().dropDatabase();
		Game game = new Game();

		game.setId(GAME);
		game.setName("test game");

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		game.getActions().add("action2");

		gameManager.saveGameDefinition(game);
		gameManager.addRule(new DBRule(GAME, "my rule"));

		game = gameManager.loadGameDefinitionById(GAME);
		Assert.assertTrue(game.getRules().size() == 1
				&& game.getRules().iterator().next().startsWith("db://"));

	}

	@Test
	public void gameTerminate() {
		GamePersistence game = defineGame();
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.HOUR_OF_DAY, -2);
		game.setExpiration(cal.getTimeInMillis());
		game.setTerminated(false);
		gameManager.saveGameDefinition(game.toGame());
		gameManager.taskDestroyer();
		Game g = gameManager.loadGameDefinitionById(GAME);
		Assert.assertEquals(true, g.isTerminated());

	}

	@Test
	public void gameNotTerminate() {
		GamePersistence game = defineGame();
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.HOUR_OF_DAY, 2);
		game.setExpiration(cal.getTimeInMillis());
		game.setTerminated(false);
		gameManager.saveGameDefinition(game.toGame());
		gameManager.taskDestroyer();
		Game g = gameManager.loadGameDefinitionById(GAME);
		Assert.assertEquals(false, g.isTerminated());

	}

	class ExecutionData {
		private String actionId;
		private String userId;
		private Map<String, Object> data;

		public ExecutionData() {
		}

		public String getActionId() {
			return actionId;
		}

		public void setActionId(String actionId) {
			this.actionId = actionId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}

	}

	private GamePersistence defineGame() {
		Game game = new Game();

		game.setId(GAME);
		game.setName(GAME);

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		game.getActions().add("classification");

		game.setTasks(new HashSet<GameTask>());

		// final classifications
		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("0 20 * * * *");
		ClassificationTask task1 = new ClassificationTask(schedule, 3,
				"green leaves", "final classification green");
		game.getTasks().add(task1);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task2 = new ClassificationTask(schedule, 3,
				"health", "final classification health");
		game.getTasks().add(task2);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task3 = new ClassificationTask(schedule, 3, "p+r",
				"final classification p+r");
		game.getTasks().add(task3);

		// week classifications // schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task4 = new ClassificationTask(schedule, 1,
				"green leaves", "week classification green");
		game.getTasks().add(task4);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task5 = new ClassificationTask(schedule, 1,
				"health", "week classification health");
		game.getTasks().add(task5);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task6 = new ClassificationTask(schedule, 1, "p+r",
				"week classification p+r");
		game.getTasks().add(task6);

		return new GamePersistence(game);

	}

}
