package eu.trentorise.game.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
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
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.task.ClassificationTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GameEngineTest {

	@Autowired
	private GameManager gameManager;

	@Autowired
	private GameEngine engine;

	@Autowired
	MongoTemplate mongo;

	private static final String GAME_ID = "gameTest";
	private static final String ACTION = "action1";
	private static final String PLAYER = "iansolo";

	private Game defineGame() {
		Game game = new Game();

		game.setId(GAME_ID);
		game.setName("test game");

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		game.getActions().add("action2");
		return game;
	}

	private void initClasspathRuleGame() {
		mongo.getDb().dropDatabase();
		gameManager.saveGameDefinition(defineGame());

		// add rules

		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/initState.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/greenPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/healthBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/healthPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/prBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/prPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/specialBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/weekClassificationBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/finalClassificationBadges.drl"));
	}

	private void initDBRuleGame() {
		mongo.getDb().dropDatabase();
		gameManager.saveGameDefinition(defineGame());

		// add rules
		try {

			String c = FileUtils.readFileToString(new File(Thread
					.currentThread().getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/initState.drl")
					.getFile()));

			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/greenBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/greenPoints.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/healthPoints.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/healthBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/prPoints.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/prBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME_ID + "/specialBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"rules/" + GAME_ID
									+ "/weekClassificationBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));
			c = FileUtils.readFileToString(new File(Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"rules/" + GAME_ID
									+ "/finalClassificationBadges.drl")
					.getFile()));
			gameManager.addRule(new DBRule(GAME_ID, c));

		} catch (IOException e) {
			Assert.fail("Fail to save rule in db from classpath");
		}
	}

	/**
	 * sample usage of classification task
	 * 
	 * 
	 */
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

	@Test
	public void loadGame() {
		initClasspathRuleGame();
		Assert.assertEquals(GAME_ID, gameManager.getGameIdByAction(ACTION));
	}

	@Test
	public void execution() {
		initClasspathRuleGame();
		PlayerState p = new PlayerState(PLAYER, GAME_ID);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		params.put("bikesharing", true);
		params.put("sustainable", true);
		params.put("p+r", true);
		params.put("park", "MANIFATTURA");
		p = engine.execute(GAME_ID, p, ACTION, params);
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
	public void dbExecution() {
		initDBRuleGame();
		PlayerState p = new PlayerState(PLAYER, GAME_ID);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		params.put("bikesharing", true);
		params.put("sustainable", true);
		params.put("p+r", true);
		params.put("park", "MANIFATTURA");
		p = engine.execute(GAME_ID, p, ACTION, params);
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
	public void dbRule() {
		mongo.getDb().dropDatabase();
		Game game = new Game();

		game.setId(GAME_ID);
		game.setName("test game");

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		game.getActions().add("action2");

		gameManager.saveGameDefinition(game);
		gameManager.addRule(new DBRule(GAME_ID, "my rule"));

		game = gameManager.loadGameDefinitionById(GAME_ID);
		Assert.assertTrue(game.getRules().size() == 1
				&& game.getRules().iterator().next().startsWith("db://"));

	}
}
