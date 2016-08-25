package eu.trentorise.game.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
import eu.trentorise.game.model.BadgeCollectionConcept;
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
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.task.IncrementalClassificationTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class ClassificationTaskTest {

	@Autowired
	private GameService gameSrv;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private TaskService taskSrv;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private GameEngine engine;

	private static final long WAIT_EXEC = 15 * 1000;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
		mongo.dropCollection(ChallengeModel.class);
	}

	@Test
	public void incremental() {
		Game g = new Game();
		g.setId("test");

		PointConcept p = new PointConcept("yellow");
		final long EVERY_MINUTE = 60000;
		p.addPeriod("period1", new LocalDate().toDate(), EVERY_MINUTE);
		g.setConcepts(new HashSet<GameConcept>());
		g.getConcepts().add(p);

		g.setTasks(new HashSet<GameTask>());
		IncrementalClassificationTask incClass = new IncrementalClassificationTask(
				p, "period1", "newClassification");
		g.getTasks().add(incClass);
		g = gameSrv.saveGameDefinition(g);
		// temp and not complete fix. When GameSrv is initialized startupTask
		// runs using dirty
		// data present in game collection
		// cleaning MUST be improved
		taskSrv.destroyTask(incClass, g.getId());
		taskSrv.createTask(incClass, g.getId());
	}

	@Test
	public void incrementalSample() throws InterruptedException {
		final String GAME = "classification";
		final String OWNER = "testOwner";
		final String ACTION = "myAction";
		final String PLAYER = "myPlayer";

		Game game = new Game();

		game.setId(GAME);
		game.setName(GAME);
		game.setOwner(OWNER);

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);

		PointConcept green = new PointConcept("green leaves");
		green.addPeriod("important", new LocalDate().toDate(), 1000l);

		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(new PointConcept("green leaves"));
		game.getConcepts().add(new PointConcept("health"));
		game.getConcepts().add(new BadgeCollectionConcept("green leaves"));

		game.setTasks(new HashSet<GameTask>());

		IncrementalClassificationTask task = new IncrementalClassificationTask(
				green, "important", "final classification green");

		game.getTasks().add(task);

		gameSrv.saveGameDefinition(game);

		// add rules
		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME
				+ "/constants");
		rule.setName("constants");
		gameSrv.addRule(rule);
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenBadges.drl"));
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthPoints.drl"));
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/finalClassificationBadges.drl"));

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
}
