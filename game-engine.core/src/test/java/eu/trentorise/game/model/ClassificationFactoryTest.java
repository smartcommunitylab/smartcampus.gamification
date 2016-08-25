package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.managers.ClassificationFactory;
import eu.trentorise.game.model.core.ClassificationPosition;

/*
 * Test is in this package instead of managers to use package visibility of executionMoment
 * TO FIX
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class ClassificationFactoryTest {

	private static final String GAME = "classificationFactoryGame";

	private static final long ONE_DAY_MILLIS = 24 * 60 * 60000;
	private static final long TWO_DAYS_MILLIS = 2 * ONE_DAY_MILLIS;

	private LocalDate today = new LocalDate();
	private LocalDate startPeriod = today.minusDays(3);

	private PointConcept createPointRed() {
		PointConcept p1 = new PointConcept("red");
		p1.addPeriod("period1", startPeriod.toDate(), ONE_DAY_MILLIS);
		return p1;
	}

	private PointConcept createPointYellow() {
		PointConcept p1 = new PointConcept("yellow");
		p1.addPeriod("period1", startPeriod.toDate(), TWO_DAYS_MILLIS);
		return p1;
	}

	/*
	 * Eddie Brock - red : today 20 today-1 10 - yellow: today 50
	 * 
	 * Cletus Casady - red: today 12 today-1 5 - yellow: today 75
	 * 
	 * Tanis Nieves - red: today 7 today-1 12 - yellow: today 34
	 * 
	 * Patrick Mulligan - red: today 51 today-1 4 - yellow: today 16
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
	public void incremental() {

		// incremental current period
		List<ClassificationPosition> c1 = ClassificationFactory
				.createIncrementalClassification(getStates1(), "red", "period1")
				.getClassification();

		Assert.assertEquals("Patrick Mulligan", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Cletus Casady", c1.get(2).getPlayerId());
		Assert.assertEquals("Tanis Nieves", c1.get(3).getPlayerId());

		c1 = ClassificationFactory.createIncrementalClassification(
				getStates1(), "yellow", "period1").getClassification();

		Assert.assertEquals("Cletus Casady", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Tanis Nieves", c1.get(2).getPlayerId());
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());

		// incremental given a date (today-1)
		c1 = ClassificationFactory.createIncrementalClassification(
				getStates1(), "red", "period1",
				new DateTime().withTimeAtStartOfDay().minusDays(1).toDate())
				.getClassification();

		Assert.assertEquals("Tanis Nieves", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Cletus Casady", c1.get(2).getPlayerId());
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());

		// incremental yellow given end instance date
		c1 = ClassificationFactory.createIncrementalClassification(
				getStates1(), "yellow", "period1",
				startPeriod.plusDays(2).toDate()).getClassification();

		Assert.assertEquals("Cletus Casady", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Tanis Nieves", c1.get(2).getPlayerId());
		Assert.assertEquals("Patrick Mulligan", c1.get(3).getPlayerId());

		c1 = ClassificationFactory.createIncrementalClassification(
				getStates1(),
				"yellow",
				"period1",
				new DateTime().withDate(startPeriod.plusDays(2))
						.withTimeAtStartOfDay().minusMinutes(1).toDate())
				.getClassification();

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
								.toDate()).getClassification();

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
								.toDate()).getClassification();

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
				.getClassification();

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
				.getClassification();

		Assert.assertEquals("Patrick Mulligan", c1.get(0).getPlayerId());
		Assert.assertEquals("Eddie Brock", c1.get(1).getPlayerId());
		Assert.assertEquals("Tanis Nieves", c1.get(2).getPlayerId());
		Assert.assertEquals("Cletus Casady", c1.get(3).getPlayerId());
	}

}
