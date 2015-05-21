package eu.trentorise.game.managers;

import java.util.HashSet;

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
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class CustomDataTest {

	private static final String GAME = "customData";
	private static final String PLAYER = "player1";
	@Autowired
	private GameManager gameManager;

	@Autowired
	private GameEngine engine;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private GameWorkflow workflow;

	@Autowired
	private MongoTemplate mongo;

	private Game defineGame() {
		Game game = new Game();

		game.setId(GAME);
		game.setName(GAME);

		game.setActions(new HashSet<String>());
		game.getActions().add("write-data");
		game.getActions().add("edit-data");

		game.setConcepts(new HashSet<GameConcept>());
		game.setTasks(new HashSet<GameTask>());
		return game;

	}

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void workflow() throws InterruptedException {
		Game game = gameManager.saveGameDefinition(defineGame());
		ClasspathRule rule = new ClasspathRule(game.getId(), "rules/" + GAME
				+ "/rule1.drl");
		gameManager.addRule(rule);

		PlayerState state = playerSrv.loadState(PLAYER, game.getId());
		Assert.assertEquals(0, state.getCustomData().size());
		workflow.apply(game.getId(), "write-data", PLAYER, null, null);
		Thread.sleep(5000);
		state = playerSrv.loadState(PLAYER, game.getId());
		Assert.assertEquals(1, state.getCustomData().size());
		Assert.assertEquals(1000,
				state.getCustomData().get(0).getData().get("counter"));

		workflow.apply(game.getId(), "edit-data", PLAYER, null, null);
		Thread.sleep(5000);
		state = playerSrv.loadState(PLAYER, game.getId());
		Assert.assertEquals(1, state.getCustomData().size());
		Assert.assertEquals(1010,
				state.getCustomData().get(0).getData().get("counter"));

		workflow.apply(game.getId(), "edit-data", PLAYER, null, null);
		Thread.sleep(5000);
		state = playerSrv.loadState(PLAYER, game.getId());
		Assert.assertEquals(1, state.getCustomData().size());
		Assert.assertEquals(1020,
				state.getCustomData().get(0).getData().get("counter"));
	}
}
