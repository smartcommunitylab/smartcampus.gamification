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
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.PlayerService;
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
	private PlayerService playerSrv;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private GameWorkflow workflow;

	private static final String GAME = "coreGameTest";
	private static final String ACTION = "save_itinerary";
	private static final String PLAYER = "iansolo";
	private static final String OWNER = "chewbecca";

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
		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME
				+ "/constants");
		rule.setName("constants");
		gameManager.addRule(rule);
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

			String c = FileUtils.readFileToString(new File(Thread
					.currentThread().getContextClassLoader()
					.getResource("rules/" + GAME + "/constants").getFile()));
			DBRule rule = new DBRule(GAME, c);
			rule.setName("constants");
			gameManager.addRule(rule);

			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/greenBadges.drl")
					.getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("greenBadges");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/greenPoints.drl")
					.getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("greenPoints");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/healthPoints.drl")
					.getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("healthPoints");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/healthBadges.drl")
					.getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("healthBadges");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/prPoints.drl").getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("prPoints");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/prBadges.drl").getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("prBadges");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource("rules/" + GAME + "/specialBadges.drl")
					.getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("specialBadges");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"rules/" + GAME + "/weekClassificationBadges.drl")
					.getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("weekClassificationBadges");
			gameManager.addRule(rule);
			c = FileUtils.readFileToString(new File(Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"rules/" + GAME + "/finalClassificationBadges.drl")
					.getFile()));
			rule = new DBRule(GAME, c);
			rule.setName("finalClassificationBadges");
			gameManager.addRule(rule);

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
		PlayerState p = playerSrv.loadState(GAME, PLAYER, true);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		params.put("bikesharing", true);
		params.put("sustainable", true);
		params.put("p+r", true);
		params.put("park", "MANIFATTURA");
		p = engine.execute(GAME, p, ACTION, params, null);
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
	public void dbExecution() throws InterruptedException {
		initDBRuleGame();
		PlayerState p = playerSrv.loadState(GAME, PLAYER, true);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		params.put("bikesharing", true);
		params.put("sustainable", true);
		params.put("p+r", true);
		params.put("park", "MANIFATTURA");
		p = engine.execute(GAME, p, ACTION, params, null);
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
	public void gameConcepts() {
		mongo.save(defineGame());
		Game g = gameManager.loadGameDefinitionById(GAME);
		Assert.assertEquals(7, g.getConcepts().size());
		for (GameConcept gc : g.getConcepts()) {
			if (gc instanceof PointConcept) {
				Assert.assertTrue(gc.getName().equals("green leaves")
						|| gc.getName().equals("health")
						|| gc.getName().equals("p+r"));
			}

			if (gc instanceof BadgeCollectionConcept) {
				Assert.assertTrue(gc.getName().equals("green leaves")
						|| gc.getName().equals("health")
						|| gc.getName().equals("p+r")
						|| gc.getName().equals("special"));
			}
		}
	}

	@Test
	public void initPlayerState() {
		initClasspathRuleGame();
		workflow.apply(GAME, ACTION, "25", null, null);
		PlayerState p = playerSrv.loadState(GAME, "25", true);
		Assert.assertEquals(7, p.getState().size());
	}

	@Test
	public void owner() {
		initClasspathRuleGame();
		Game g = gameManager.loadGameDefinitionById(GAME);
		Assert.assertEquals(OWNER, g.getOwner());
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
		game.setOwner(OWNER);

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);

		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(new PointConcept("green leaves"));
		game.getConcepts().add(new PointConcept("health"));
		game.getConcepts().add(new PointConcept("p+r"));
		game.getConcepts().add(new BadgeCollectionConcept("green leaves"));
		game.getConcepts().add(new BadgeCollectionConcept("health"));
		game.getConcepts().add(new BadgeCollectionConcept("p+r"));
		game.getConcepts().add(new BadgeCollectionConcept("special"));

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
