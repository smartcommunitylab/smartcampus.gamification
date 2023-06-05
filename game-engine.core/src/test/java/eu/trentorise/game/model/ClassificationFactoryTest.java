package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.RabbitConf;
import eu.trentorise.game.managers.ClassificationFactory;
import eu.trentorise.game.model.core.ClassificationPosition;

/*
 * Test is in this package instead of managers to use package visibility of executionMoment
 * TO FIX
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class, RabbitConf.class, BraveAutoConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class ClassificationFactoryTest {

	private static final String GAME = "classificationFactoryGame";

	private static final long RED_PERIOD1_DURATION = 24 * 60 * 60000; // one day

	private static final long YELLOW_PERIOD1_DURATION = 2 * RED_PERIOD1_DURATION; // two
																					// days

	private LocalDate today = new LocalDate();
	private LocalDate startPeriod = today.minusDays(1);

	private PointConcept createPointRed() {
		PointConcept p1 = new PointConcept("red");
		p1.addPeriod("period1", startPeriod.toDate(), RED_PERIOD1_DURATION);
		return p1;
	}

	private PointConcept createPointYellow() {
		PointConcept p1 = new PointConcept("yellow");
		p1.addPeriod("period1", startPeriod.toDate(), YELLOW_PERIOD1_DURATION);
		return p1;
	}

	/*
	 * Eddie Brock - red : today-1 10 today 20 - yellow: today 50
	 * 
	 * Cletus Casady - red: today-1 5 today 12 - yellow: today 75
	 * 
	 * Tanis Nieves - red: today-1 12 today 7 - yellow: today 34
	 * 
	 * Patrick Mulligan - red: today-1 4 today 51 - yellow: today 16
	 */
	private List<PlayerState> getStates1() {
		List<PlayerState> players = new ArrayList<>();
		PlayerState p = new PlayerState(GAME, "Eddie Brock");
		PointConcept red = createPointRed();
		long now = new Date().getTime();
		long deepInPast = new DateTime().minusDays(1).withTimeAtStartOfDay()
				.plusHours(1).getMillis();

		red.executionMoment = deepInPast;
		red.increment(10d); // today-1 10
		red.executionMoment = now;
		red.increment(20d); // today 20

		PointConcept yellow = createPointYellow();
		yellow.setScore(50d);

		p.getState().add(red);
		p.getState().add(yellow);
		players.add(p);

		p = new PlayerState(GAME, "Cletus Casady");
		red = createPointRed();

		red.executionMoment = deepInPast;
		red.increment(5d); // today-1 5
		red.executionMoment = now;
		red.increment(12d); // today 12

		yellow = createPointYellow();
		yellow.setScore(75d);

		p.getState().add(red);
		p.getState().add(yellow);
		players.add(p);

		p = new PlayerState(GAME, "Tanis Nieves");
		red = createPointRed();

		red.executionMoment = deepInPast;
		red.increment(12d); // today-1 12
		red.executionMoment = now;
		red.increment(7d); // today 7

		yellow = createPointYellow();
		yellow.setScore(34d);

		p.getState().add(red);
		p.getState().add(yellow);
		players.add(p);

		p = new PlayerState(GAME, "Patrick Mulligan");
		red = createPointRed();

		red.executionMoment = deepInPast;
		red.increment(4d); // today-1 4
		red.executionMoment = now;
		red.increment(51d); // today 51

		yellow = createPointYellow();
		yellow.setScore(16d);

		p.getState().add(red);
		p.getState().add(yellow);
		players.add(p);

		return players;
	}

	@Test
	public void incrementalUsingInstanceIdx() {

		// incremental for today-1 -> index 0
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(getStates1(), "red",
						"period1", 0).getClassificationBoard().getBoard();

		Assert.assertEquals("Tanis Nieves", c1.get(0).getPlayerId());
		Assert.assertEquals(12d, c1.get(0).getScore(), 0d);
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals(10d, c1.get(1).getScore(), 0d);
		Assert.assertEquals("Cletus Casady", c1.get(2).getPlayerId());
		Assert.assertEquals(5d, c1.get(2).getScore(), 0d);
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());
		Assert.assertEquals(4d, c1.get(3).getScore(), 0d);

		// incremental yellow
		c1 = ClassificationFactory
				.createIncrementalClassification(getStates1(), "yellow",
						"period1", 0).getClassificationBoard().getBoard();

		Assert.assertEquals("Cletus Casady", c1.get(0).getPlayerId());
		Assert.assertEquals(75d, c1.get(0).getScore(), 0d);
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals(50d, c1.get(1).getScore(), 0d);
		Assert.assertEquals("Tanis Nieves", c1.get(2).getPlayerId());
		Assert.assertEquals(34d, c1.get(2).getScore(), 0d);
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());
		Assert.assertEquals(16d, c1.get(3).getScore(), 0d);

	}

	@Test
	public void incrementalByIndexInTheFuture() {
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(getStates1(), "red",
						"period1", 20).getClassificationBoard().getBoard();

		Assert.assertEquals(0d, c1.get(0).getScore(), 0d);
		Assert.assertEquals(0d, c1.get(1).getScore(), 0d);
		Assert.assertEquals(0d, c1.get(2).getScore(), 0d);
		Assert.assertEquals(0d, c1.get(3).getScore(), 0d);
	}

	@Test
	public void incrementalWithoutParams() {
		// incremental current period
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(getStates1(), "red", "period1")
				.getClassificationBoard().getBoard();

		Assert.assertEquals("Patrick Mulligan", c1.get(0).getPlayerId());
		Assert.assertEquals(51d, c1.get(0).getScore(), 0d);
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals(20d, c1.get(1).getScore(), 0d);
		Assert.assertEquals("Cletus Casady", c1.get(2).getPlayerId());
		Assert.assertEquals(12d, c1.get(2).getScore(), 0d);
		Assert.assertEquals("Tanis Nieves", c1.get(3).getPlayerId());
		Assert.assertEquals(7d, c1.get(3).getScore(), 0d);

		c1 = ClassificationFactory
				.createIncrementalClassification(getStates1(), "yellow",
						"period1").getClassificationBoard().getBoard();

		Assert.assertEquals("Cletus Casady", c1.get(0).getPlayerId());
		Assert.assertEquals(75d, c1.get(0).getScore(), 0d);
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals(50d, c1.get(1).getScore(), 0d);
		Assert.assertEquals("Tanis Nieves", c1.get(2).getPlayerId());
		Assert.assertEquals(34d, c1.get(2).getScore(), 0d);
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());
		Assert.assertEquals(16d, c1.get(3).getScore(), 0d);
	}

	@Test
	public void incrementalByDate() {
		// incremental given a date (today-1)
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(
						getStates1(),
						"red",
						"period1",
						new DateTime().withDate(startPeriod).plusMinutes(1)
								.toDate()).getClassificationBoard().getBoard();

		Assert.assertEquals("Tanis Nieves", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Cletus Casady", c1.get(2).getPlayerId());
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());

		// incremental yellow given end instance date
		c1 = ClassificationFactory
				.createIncrementalClassification(
						getStates1(),
						"yellow",
						"period1",
						new DateTime().withDate(startPeriod).plusMinutes(1)
								.toDate()).getClassificationBoard().getBoard();

		Assert.assertEquals("Cletus Casady", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Tanis Nieves", c1.get(2).getPlayerId());
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());
	}

	@Test
	public void incrementalByEndLastInstanceDate() {
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(
						getStates1(),
						"yellow",
						"period1",
						new DateTime().withDate(startPeriod)
								.withDurationAdded(YELLOW_PERIOD1_DURATION, 1)
								.toDate()).getClassificationBoard().getBoard();

		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(0).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(1).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(2).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(3).getScore()));

	}

	@Test
	public void incrementalByFutureDate() {
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(
						getStates1(),
						"yellow",
						"period1",
						new DateTime().withDate(startPeriod.plusDays(3))
								.withTimeAtStartOfDay().minusMinutes(1)
								.toDate()).getClassificationBoard().getBoard();

		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(0).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(1).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(2).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(3).getScore()));
	}

	@Test
	public void pointNotExist() {
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(
						getStates1(),
						"blue",
						"period1",
						new DateTime().withTimeAtStartOfDay().minusDays(1)
								.toDate()).getClassificationBoard().getBoard();

		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(0).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(1).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(2).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(3).getScore()));
	}

	@Test
	public void periodNotExist() {
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(
						getStates1(),
						"yellow",
						"period11",
						new DateTime().withTimeAtStartOfDay().minusDays(1)
								.toDate()).getClassificationBoard().getBoard();

		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(0).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(1).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(2).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(3).getScore()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void momentBeforePeriodStart() {
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(getStates1(), "yellow",
						"period1", startPeriod.minusDays(1).toDate())
				.getClassificationBoard().getBoard();

		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(0).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(1).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(2).getScore()));
		Assert.assertEquals(Double.valueOf(0),
				Double.valueOf(c1.get(3).getScore()));
	}

	@Test
	public void general() {

		List<ClassificationPosition> c1 = ClassificationFactory
				.createGeneralClassification(getStates1(), "red")
				.getClassificationBoard().getBoard();

		Assert.assertEquals("Patrick Mulligan", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Tanis Nieves", c1.get(2).getPlayerId());
		Assert.assertEquals("Cletus Casady", c1.get(3).getPlayerId());
	}

}
