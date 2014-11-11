package eu.trentorise;

import java.util.HashSet;
import java.util.Set;

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
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.task.ClassificationTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class DBTestEnv {

	@Autowired
	MongoTemplate db;

	private static final String GAME = "game1";

	@Before
	public void cleanDb() {
		// clean db
		db.dropCollection(StatePersistence.class);
		db.dropCollection(GamePersistence.class);
		db.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void simpleEnv() {

		// define game
		GamePersistence game = defineGame();
		db.save(game);

		// define player states

		db.save(definePlayerState("1", 15d, 2d, 5d));
		db.save(definePlayerState("2", 12d, 50d, 12d));
		db.save(definePlayerState("11", 112d, 52d, 1d));
		db.save(definePlayerState("12", 2d, 20d, 11d));

	}

	@Test
	public void sameResult() {
		// define game
		GamePersistence game = defineGame();
		db.save(game);

		db.save(definePlayerState("1", 1d, 52d, 5d));
		db.save(definePlayerState("2", 15d, 50d, 12d));
		db.save(definePlayerState("11", 15d, 0d, 1d));
		db.save(definePlayerState("12", 2d, 52d, 11d));
	}

	@Test
	public void sameResultLastElement() {
		GamePersistence game = defineGame();
		db.save(game);

		db.save(definePlayerState("1", 12d, 52d, 5d));
		db.save(definePlayerState("2", 115d, 50d, 12d));
		db.save(definePlayerState("11", 115d, 0d, 1d));
		db.save(definePlayerState("12", 12d, 52d, 11d));
		db.save(definePlayerState("2442", 12d, 52d, 11d));
		db.save(definePlayerState("242", 4d, 52d, 11d));
		db.save(definePlayerState("244", 5d, 52d, 11d));
	}

	private StatePersistence definePlayerState(String playerId,
			Double greenPoint, Double healthPoint, Double prPoint) {
		PlayerState player = new PlayerState();
		player.setGameId(GAME);
		player.setPlayerId(playerId);
		Set<GameConcept> myState = new HashSet<GameConcept>();
		PointConcept pc = new PointConcept("green leaves");
		pc.setScore(greenPoint);
		myState.add(pc);
		pc = new PointConcept("health");
		pc.setScore(healthPoint);
		myState.add(pc);
		pc = new PointConcept("p+r");
		pc.setScore(prPoint);
		myState.add(pc);
		BadgeCollectionConcept badge = new BadgeCollectionConcept();
		myState.add(badge);
		player.setState(myState);

		return new StatePersistence(player);
	}

	private GamePersistence defineGame() {
		Game game = new Game();
		game.setId("game1");
		game.setName("rovereto explorer");

		game.setActions(new HashSet<String>());
		game.getActions().add("save_itinerary");
		game.getActions().add("classification");

		game.setRules(new HashSet<String>());

		game.getRules().add("finalClassificationBadges.drl");
		game.getRules().add("greenBadges.drl");
		game.getRules().add("greenPoints.drl");
		game.getRules().add("healthBadges.drl");
		game.getRules().add("healthPoints.drl");
		game.getRules().add("initState.drl");
		game.getRules().add("prBadges.drl");
		game.getRules().add("prPoints.drl");
		game.getRules().add("specialBadges.drl");
		game.getRules().add("weekClassificationBadges.drl");

		game.setTasks(new HashSet<GameTask>());

		// final classifications
		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("*/55 * * * * *");
		ClassificationTask task1 = new ClassificationTask(schedule, 3,
				"green leaves", "final classification green");
		game.getTasks().add(task1);

		// schedule = new TaskSchedule();
		// schedule.setCronExpression("0 5 * * * *");
		// ClassificationTask task2 = new ClassificationTask(schedule, 3,
		// "health", "final classification health");
		// game.getTasks().add(task2);
		//
		// schedule = new TaskSchedule();
		// schedule.setCronExpression("0 6 * * * *");
		// ClassificationTask task3 = new ClassificationTask(schedule, 3, "p+r",
		// "final classification p+r");
		// game.getTasks().add(task3);

		// week classifications
		// schedule = new TaskSchedule();
		// schedule.setCronExpression("0 0 * * * *");
		// ClassificationTask task4 = new ClassificationTask(schedule, 1,
		// "green leaves", "week classification green");
		// game.getTasks().add(task4);
		//
		schedule = new TaskSchedule();
		schedule.setCronExpression("0/30 * * * * *");
		ClassificationTask task5 = new ClassificationTask(schedule, 1,
				"health", "week classification health");
		game.getTasks().add(task5);
		//
		// schedule = new TaskSchedule();
		// schedule.setCronExpression("0 2 * * * *");
		// ClassificationTask task6 = new ClassificationTask(schedule, 1, "p+r",
		// "week classification p+r");
		// game.getTasks().add(task6);
		return new GamePersistence(game);

	}
}
