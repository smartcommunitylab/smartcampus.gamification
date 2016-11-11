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

	private static final long HOUR_MILLISEC = 3600000;
	private static final long DAY_MILLISEC = HOUR_MILLISEC * 24;
	private static final long WEEK_MILLISEC = DAY_MILLISEC * 7;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.getDb().dropDatabase();
	}

	@Test
	public void addAPeriod() {
		PointConcept pc = new PointConcept("testPoint");
		LocalDate start1 = new LocalDate();
		pc.addPeriod("period1", start1.toDate(), DAY_MILLISEC);
		pc.setScore(15d);
		pc.setScore(25d);

		Assert.assertEquals(new Double(25), pc.getPeriodCurrentScore(0));
	}

	@Test
	public void persistPeriod() {
		final String GAME_ID = "TEST_GAME";
		final String PLAYER_ID = "1000";

		PointConcept pc = new PointConcept("testPoint");
		LocalDate start1 = new LocalDate();
		pc.addPeriod("period1", start1.toDate(), DAY_MILLISEC);
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
		LocalDate start = new LocalDate();
		pc.addPeriod("period1", start.toDate(), WEEK_MILLISEC);

		start = new LocalDate();
		pc.addPeriod("period2", start.toDate(), DAY_MILLISEC);

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
		LocalDate start = new LocalDate().minusDays(1);
		pc.addPeriod("period1", start.toDate(), DAY_MILLISEC);

		start = new LocalDate();
		pc.addPeriod("period2", start.toDate(), HOUR_MILLISEC);

		DateTime executionTime = new DateTime();

		pc.executionMoment = executionTime.minusDays(1).getMillis();
		pc.setScore(10d);

		pc.executionMoment = executionTime.getMillis();
		pc.setScore(20d); // +10

		pc.executionMoment = executionTime.minusHours(1).getMillis();
		pc.setScore(21d); // +1
		pc.setScore(24d); // +3

		pc.executionMoment = executionTime.plusHours(1).getMillis();
		pc.setScore(25d);// +1

		pc.executionMoment = executionTime.plusHours(2).getMillis();
		pc.setScore(39d); // +14

		PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
		ps.getState().add(pc);
		ps = playerSrv.saveState(ps);

		PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false);

		PointConcept p = (PointConcept) loaded.getState().iterator().next();
		p.executionMoment = pc.executionMoment;

		/*
		 * period1 : day-1 10, now 29
		 * 
		 * period2 : hour-1 4, now 10, hour+1 1, hour+2 14
		 */

		Assert.assertEquals(new Double(29), p.getPeriodCurrentScore("period1"));
		Assert.assertEquals(new Double(14), p.getPeriodCurrentScore("period2"));
		Assert.assertEquals(new Double(10), p.getPeriodPreviousScore("period1"));
		Assert.assertEquals(new Double(1), p.getPeriodPreviousScore("period2"));
		Assert.assertEquals(new Double(0), p.getPeriodPreviousScore("period45"));
		Assert.assertEquals(new Double(29), p.getPeriodScore("period1", 0));
		Assert.assertEquals(new Double(14), p.getPeriodScore("period2", 0));
		Assert.assertEquals(new Double(1), p.getPeriodScore("period2", 1));
		Assert.assertEquals(new Double(10), p.getPeriodScore("period2", 2));
		Assert.assertEquals(new Double(14), p.getPeriodScore("period2",
				executionTime.plusHours(2).getMillis()));
		Assert.assertEquals(new Double(10),
				p.getPeriodScore("period2", executionTime.getMillis()));

		Assert.assertEquals(new Double(0), p.getPeriodScore("period2",
				executionTime.plusDays(1).getMillis()));

		Assert.assertEquals(new Double(0), p.getPeriodScore("period2", 10));

		Assert.assertEquals(Double.valueOf(14),
				p.getPeriodCurrentInstance("period2").getScore());

		DateTime testTime = DateTime.now().withMinuteOfHour(0)
				.withSecondOfMinute(0).withMillisOfSecond(0); // current Hour

		Assert.assertEquals(testTime.plusHours(2).getMillis(), p
				.getPeriodCurrentInstance("period2").getStart());
		Assert.assertEquals(testTime.plusHours(2).getMillis() + HOUR_MILLISEC,
				p.getPeriodCurrentInstance("period2").getEnd());

		Assert.assertEquals(Double.valueOf(14),
				p.getPeriodInstance("period2", 0).getScore());
		Assert.assertEquals(testTime.plusHours(2).getMillis(), p
				.getPeriodInstance("period2", 0).getStart());
		Assert.assertEquals(testTime.plusHours(2).getMillis() + HOUR_MILLISEC,
				p.getPeriodInstance("period2", 0).getEnd());

		Assert.assertNull(p.getPeriodInstance("period2", 20));

		Assert.assertEquals(new Double(10), p.getPeriodInstance("period2", 2)
				.getScore());
		Assert.assertEquals(testTime.getMillis(),
				p.getPeriodInstance("period2", 2).getStart());
		Assert.assertEquals(testTime.getMillis() + HOUR_MILLISEC, p
				.getPeriodInstance("period2", 2).getEnd());

		p.executionMoment = executionTime.plusDays(2).getMillis();
		Assert.assertEquals(new Double(0), p.getPeriodPreviousScore("period1"));
		Assert.assertEquals(new Double(0),
				p.getPeriodPreviousInstance("period1").getScore());

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
		pc.addPeriod("period1", new LocalDate().toDate(), DAY_MILLISEC);
		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(pc);

		gameSrv.saveGameDefinition(game);

		// add rules
		gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/points.drl"));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("walkDistance", 2d);
		workflow.apply(GAME, ACTION, "my player", data, null);
		Thread.sleep(30000);

		Assert.assertEquals(new Double(4),
				((PointConcept) playerSrv.loadState(GAME, "my player", false)
						.getState().iterator().next()).getScore());

	}
}
