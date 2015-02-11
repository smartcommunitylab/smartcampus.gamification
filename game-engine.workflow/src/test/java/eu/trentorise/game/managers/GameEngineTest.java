package eu.trentorise.game.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.GameEngine;

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

	private void initClasspathGame() {
		mongo.getDb().dropDatabase();
		gameManager.saveGameDefinition(defineGame());

		// add rules

		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/initState.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME_ID, "rules/" + GAME_ID
				+ "/greenPoints.drl"));
	}

	private void initDBGame() {
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
		} catch (IOException e) {
			Assert.fail("Fail to save rule in db from classpath");
		}
	}

	@Test
	public void loadGame() {
		initClasspathGame();
		Assert.assertEquals(GAME_ID, gameManager.getGameIdByAction(ACTION));
	}

	@Test
	public void execution() {
		initClasspathGame();
		PlayerState p = new PlayerState(PLAYER, GAME_ID);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		p = engine.execute(GAME_ID, p, ACTION, params);
		// expected 60 greenPoints and earned 10-point 50-point green badges
		boolean found = false;
		for (GameConcept gc : p.getState()) {
			if (gc instanceof PointConcept
					&& gc.getName().equals("green leaves")) {
				found = true;
				Assert.assertEquals(60d, ((PointConcept) gc).getScore()
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
		initDBGame();
		PlayerState p = new PlayerState(PLAYER, GAME_ID);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bikeDistance", 8.43);
		params.put("walkDistance", 3.100);
		p = engine.execute(GAME_ID, p, ACTION, params);
		// expected 60 greenPoints and earned 10-point 50-point green badges
		boolean found = false;
		for (GameConcept gc : p.getState()) {
			if (gc instanceof PointConcept
					&& gc.getName().equals("green leaves")) {
				found = true;
				Assert.assertEquals(60d, ((PointConcept) gc).getScore()
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
