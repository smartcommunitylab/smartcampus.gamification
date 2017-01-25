package eu.trentorise.game.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;
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
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class ChallengeTest {

	@Autowired
	private GameService gameSrv;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private GameEngine engine;

	@Autowired
	private MongoTemplate mongo;

	private static final String GAME = "challengeGameTest";
	private static final String PLAYER = "eddie brock";
	private static final String ACTION = "beatme";

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
		mongo.dropCollection(ChallengeModel.class);
	}

	@Test
	public void loadChallenges() {
		Game game = gameSrv.saveGameDefinition(defineGame());

		// add rules
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/challenges.drl"));
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));

		// define challenge Model
		ChallengeModel model1 = new ChallengeModel();
		model1.setName("prize");
		gameSrv.saveChallengeModel(GAME, model1);

		LocalDate now = new LocalDate();
		playerSrv.assignChallenge(GAME, PLAYER, "prize", null, null,
				now.toDate(), now.dayOfMonth().addToCopy(2).toDate());

		PlayerState p = playerSrv.loadState(GAME, PLAYER, false);

		// execution
		p = engine.execute(GAME, p, ACTION, null, UUID.randomUUID().toString(),
				System.currentTimeMillis(), null);

		Assert.assertEquals(2, p.getState().size());
		ChallengeConcept ch = null;
		for (GameConcept gc : p.getState()) {
			if (gc instanceof ChallengeConcept) {
				ch = (ChallengeConcept) gc;
			}
		}

		Assert.assertTrue(ch.isCompleted());
	}

	@Test
	public void endedChallenges() {
		Game game = gameSrv.saveGameDefinition(defineGame());

		// add rules
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/challenges.drl"));
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));

		// define challenge Model
		ChallengeModel model1 = new ChallengeModel();
		model1.setName("prize");
		gameSrv.saveChallengeModel(GAME, model1);

		LocalDate now = new LocalDate();
		playerSrv.assignChallenge(GAME, PLAYER, "prize", null, null, now
				.dayOfMonth().addToCopy(-2).toDate(), now.dayOfMonth()
				.addToCopy(-1).toDate());

		playerSrv.assignChallenge(GAME, PLAYER, "prize", null, null, now
				.dayOfMonth().addToCopy(5).toDate(), now.dayOfMonth()
				.addToCopy(7).toDate());

		PlayerState p = playerSrv.loadState(GAME, PLAYER, false);

		// execution
		p = engine.execute(GAME, p, ACTION, null, UUID.randomUUID().toString(),
				System.currentTimeMillis(), null);

		Assert.assertEquals(3, p.getState().size());
		ChallengeConcept ch = null;
		for (GameConcept gc : p.getState()) {
			if (gc instanceof ChallengeConcept) {
				ch = (ChallengeConcept) gc;
				Assert.assertFalse(ch.isCompleted());
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongAssignemnt() {
		Game game = gameSrv.saveGameDefinition(defineGame());

		// add rules
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/challenges.drl"));
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));

		// define challenge Model
		ChallengeModel model1 = new ChallengeModel();
		model1.setName("prize");
		gameSrv.saveChallengeModel(GAME, model1);

		LocalDate now = new LocalDate();
		playerSrv.assignChallenge(GAME, PLAYER, "prize", null, null, now
				.dayOfMonth().addToCopy(-2).toDate(), now.dayOfMonth()
				.addToCopy(-1).toDate());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wrongField", 50);
		playerSrv.assignChallenge(GAME, PLAYER, "prize", null, params, now
				.dayOfMonth().addToCopy(5).toDate(), now.dayOfMonth()
				.addToCopy(7).toDate());

	}

	private Game defineGame() {

		final String OWNER = "gameMaster";

		Game game = new Game();

		game.setId(GAME);
		game.setName(GAME);
		game.setOwner(OWNER);

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);

		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(new PointConcept("green leaves"));

		game.setTasks(new HashSet<GameTask>());
		return game;

	}
}
