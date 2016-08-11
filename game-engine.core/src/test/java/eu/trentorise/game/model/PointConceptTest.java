package eu.trentorise.game.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.joda.time.DateTime;
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
import eu.trentorise.game.managers.GameWorkflow;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class PointConceptTest {

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private GameService gameSrv;

	@Autowired
	private GameWorkflow workflow;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void addAPeriod() {
		PointConcept pc = new PointConcept("testPoint");
		LocalDate start1 = new LocalDate(2016, 7, 10);
		long period1 = 7 * 24 * 3600 * 1000; // one week millisec
		pc.addPeriod("period1", start1.toDate(), period1);
		pc.setScore(15d);
		pc.setScore(25d);

		Assert.assertEquals(new Double(25), pc.getPeriodCurrentScore(0));
	}

	@Test
	public void persistPeriod() {
		final String GAME_ID = "TEST_GAME";
		final String PLAYER_ID = "1000";

		PointConcept pc = new PointConcept("testPoint");
		LocalDate start1 = new LocalDate(2016, 7, 10);
		long period1 = 7 * 24 * 3600 * 1000; // one week millisec
		pc.addPeriod("period1", start1.toDate(), period1);
		pc.setScore(15d);
		pc.setScore(35d);

		PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
		ps.getState().add(pc);
		ps = playerSrv.saveState(ps);

		PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false);

		PointConcept p = (PointConcept) loaded.getState().iterator().next();

		Assert.assertEquals(new Double(35), p.getPeriodCurrentScore(0));
	}

	@Test
	public void morePeriods() {
		final String GAME_ID = "TEST_GAME";
		final String PLAYER_ID = "1000";

		PointConcept pc = new PointConcept("testPoint");
		LocalDate start = new LocalDate(2016, 7, 10);
		long period = 7 * 24 * 3600 * 1000; // one week millisec
		pc.addPeriod("period1", start.toDate(), period);

		start = new LocalDate(2016, 7, 25);
		period = 24 * 3600 * 1000;
		pc.addPeriod("period2", start.toDate(), period);

		pc.setScore(21d);

		PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
		ps.getState().add(pc);
		ps = playerSrv.saveState(ps);

		PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false);

		PointConcept p = (PointConcept) loaded.getState().iterator().next();

		Assert.assertEquals(new Double(21), p.getPeriodCurrentScore(0));
		Assert.assertEquals(new Double(21), p.getPeriodCurrentScore(1));

	}

	@Test
	public void differentInstances() throws InterruptedException {
		final String GAME_ID = "TEST_GAME";
		final String PLAYER_ID = "1000";

		PointConcept pc = new PointConcept("testPoint");
		LocalDate start = new LocalDate(2016, 7, 10);
		long period = 7 * 24 * 3600 * 1000; // one week millisec
		pc.addPeriod("period1", start.toDate(), period);

		start = new LocalDate(2016, 7, 25);
		period = 3600 * 1000;
		pc.addPeriod("period2", start.toDate(), period);

		DateTime executionTime = new DateTime();

		pc.executionMoment = executionTime.getMillis();
		pc.setScore(10d);

		pc.executionMoment = executionTime.hourOfDay().addToCopy(-1)
				.getMillis();
		pc.setScore(11d);
		pc.setScore(14d);

		pc.executionMoment = executionTime.hourOfDay().addToCopy(1).getMillis();
		pc.setScore(15d);

		pc.executionMoment = executionTime.hourOfDay().addToCopy(2).getMillis();
		pc.setScore(29d);

		PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
		ps.getState().add(pc);
		ps = playerSrv.saveState(ps);

		PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false);

		PointConcept p = (PointConcept) loaded.getState().iterator().next();
		p.executionMoment = pc.executionMoment;

		Assert.assertEquals(new Double(29), p.getPeriodCurrentScore("period1"));
		Assert.assertEquals(new Double(14), p.getPeriodCurrentScore("period2"));
		Assert.assertEquals(new Double(1), p.getPeriodPreviousScore("period2"));
		Assert.assertEquals(new Double(0), p.getPeriodPreviousScore("period45"));
	}

	@Test
	public void tryInRules() throws InterruptedException {
		final String GAME = "periodic";
		final String OWNER = "periodicOwner";
		final String ACTION = "incremental";

		Game game = new Game();

		game.setId(GAME);
		game.setName(GAME);
		game.setOwner(OWNER);

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);

		PointConcept pc = new PointConcept("green");
		final long ONE_DAY = 24 * 3600 * 1000;
		pc.addPeriod("period1", new LocalDate().toDate(), ONE_DAY);
		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(pc);

		gameSrv.saveGameDefinition(game);

		// add rules
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/points.drl"));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("walkDistance", 2d);
		workflow.apply(GAME, ACTION, "my player", data, null);
		Thread.sleep(30000);

		// playerSrv.loadState(GAME, "my player", false);

	}
}
