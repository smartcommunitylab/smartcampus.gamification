package eu.trentorise.game.model;

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
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class PointConceptTest {

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private MongoTemplate mongo;

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
		pc.addPeriod(start1.toDate(), period1);
		pc.setScore(15d);
		pc.setScore(25d);

		Assert.assertEquals(new Double(25), pc.getCurrentPeriodScore(0));
	}

	@Test
	public void persistPeriod() {
		final String GAME_ID = "TEST_GAME";
		final String PLAYER_ID = "1000";

		PointConcept pc = new PointConcept("testPoint");
		LocalDate start1 = new LocalDate(2016, 7, 10);
		long period1 = 7 * 24 * 3600 * 1000; // one week millisec
		pc.addPeriod(start1.toDate(), period1);
		pc.setScore(15d);
		pc.setScore(35d);

		PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
		ps.getState().add(pc);
		ps = playerSrv.saveState(ps);

		PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false);

		PointConcept p = (PointConcept) loaded.getState().iterator().next();

		Assert.assertEquals(new Double(35), p.getCurrentPeriodScore(0));
	}

	@Test
	public void morePeriods() {
		final String GAME_ID = "TEST_GAME";
		final String PLAYER_ID = "1000";

		PointConcept pc = new PointConcept("testPoint");
		LocalDate start = new LocalDate(2016, 7, 10);
		long period = 7 * 24 * 3600 * 1000; // one week millisec
		pc.addPeriod(start.toDate(), period);

		start = new LocalDate(2016, 7, 25);
		period = 24 * 3600 * 1000;
		pc.addPeriod(start.toDate(), period);

		pc.setScore(21d);

		PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
		ps.getState().add(pc);
		ps = playerSrv.saveState(ps);

		PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false);

		PointConcept p = (PointConcept) loaded.getState().iterator().next();

		Assert.assertEquals(new Double(21), p.getCurrentPeriodScore(0));
		Assert.assertEquals(new Double(21), p.getCurrentPeriodScore(1));

	}

	@Test
	public void differentInstances() throws InterruptedException {
		final String GAME_ID = "TEST_GAME";
		final String PLAYER_ID = "1000";

		PointConcept pc = new PointConcept("testPoint");
		LocalDate start = new LocalDate(2016, 7, 10);
		long period = 7 * 24 * 3600 * 1000; // one week millisec
		pc.addPeriod(start.toDate(), period);

		start = new LocalDate(2016, 7, 25);
		period = 3600 * 1000;
		pc.addPeriod(start.toDate(), period);

		DateTime executionTime = new DateTime(2016, 7, 26, 1, 5, 10);
		pc.executionMoment = executionTime.getMillis();
		pc.setScore(10d);

		pc.executionMoment = executionTime.hourOfDay().addToCopy(-1)
				.getMillis();
		pc.setScore(11d);
		pc.setScore(14d);

		pc.executionMoment = executionTime.hourOfDay().addToCopy(2).getMillis();
		pc.setScore(29d);

		PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
		ps.getState().add(pc);
		ps = playerSrv.saveState(ps);

		PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false);

		PointConcept p = (PointConcept) loaded.getState().iterator().next();
		p.executionMoment = pc.executionMoment;

		Assert.assertEquals(new Double(29), p.getCurrentPeriodScore(0));
		Assert.assertEquals(new Double(15), p.getCurrentPeriodScore(1));

	}
}
