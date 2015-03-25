package eu.trentorise.game.managers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.ClassificationTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class RoveretoGameTest {

	private static final String GAME = "coreGameTest";
	private static final String ACTION = "save_itinerary";

	private static final long WAIT_EXEC = 15 * 1000;

	@Autowired
	private GameManager gameManager;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private AppContextProvider provider;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void simpleScenario() throws InterruptedException {
		simpleEnv();
		launchTaskExecution();
		Thread.sleep(WAIT_EXEC);
		analyzeSimple();

	}

	@Test
	public void sameResultScenario() throws InterruptedException {
		sameResultEnv();
		launchTaskExecution();
		Thread.sleep(WAIT_EXEC);
		analyzeSameResult();
	}

	@Test
	public void sameResultLastElementScenario() throws InterruptedException {
		sameResultLastElementEnv();
		launchTaskExecution();
		Thread.sleep(WAIT_EXEC);
		analyzeSameResultLastResult();
	}

	@Test
	public void simpleEnv() {

		// define game
		GamePersistence game = defineGame();
		mongo.save(game);

		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME
				+ "/constants");
		rule.setName("constants");
		gameManager.addRule(rule);
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/specialBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/weekClassificationBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/finalClassificationBadges.drl"));

		// define player states

		mongo.save(definePlayerState("1", 15d, 2d, 5d));
		mongo.save(definePlayerState("2", 12d, 50d, 12d));
		mongo.save(definePlayerState("11", 112d, 52d, 1d));
		mongo.save(definePlayerState("122", 2d, 20d, 11d));

		/**
		 * result after game execution
		 * 
		 * player-1 Silver-green Bronze-pr
		 * 
		 * player-2 Bronze-green Silver-health Gold-pr
		 * 
		 * player-11 Gold-green Gold-health
		 * 
		 * player-122 Bronze-health Silver-pr
		 */

	}

	@Test
	public void sameResultLastElementEnv() {
		GamePersistence game = defineGame();
		mongo.save(game);

		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME
				+ "/constants");
		rule.setName("constants");
		gameManager.addRule(rule);
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/specialBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/weekClassificationBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/finalClassificationBadges.drl"));

		mongo.save(definePlayerState("1", 12d, 52d, 5d));
		mongo.save(definePlayerState("2", 115d, 50d, 12d));
		mongo.save(definePlayerState("11", 115d, 0d, 1d));
		mongo.save(definePlayerState("12", 12d, 52d, 11d));
		mongo.save(definePlayerState("2442", 12d, 52d, 11d));
		mongo.save(definePlayerState("242", 4d, 52d, 11d));
		mongo.save(definePlayerState("244", 5d, 52d, 11d));

		/**
		 * result after game execution
		 * 
		 * player-1 Bronze-green Gold-health
		 * 
		 * player-2 Gold-green Gold-pr
		 * 
		 * player-11 Gold-green
		 * 
		 * player-12 Bronze-green Gold-health Silver-pr
		 * 
		 * player-2442 Bronze-green Gold-health Silver-pr
		 * 
		 * player-242 Gold-health Silver-pr
		 * 
		 * player-244 Gold-health Silver-pr
		 */

	}

	@Test
	public void sameResultEnv() {
		// define game
		GamePersistence game = defineGame();
		mongo.save(game);

		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME
				+ "/constants");
		rule.setName("constants");
		gameManager.addRule(rule);
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/greenPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/healthPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/prPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/specialBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/weekClassificationBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME
				+ "/finalClassificationBadges.drl"));

		mongo.save(definePlayerState("1", 1d, 52d, 5d));
		mongo.save(definePlayerState("2", 15d, 50d, 12d));
		mongo.save(definePlayerState("11", 15d, 0d, 1d));
		mongo.save(definePlayerState("12", 2d, 52d, 11d));

		/**
		 * result after game execution
		 * 
		 * player-1 Gold-health Bronze-pr
		 * 
		 * player-2 Gold-green Bronze-health Gold-pr
		 * 
		 * player-11 Gold-green
		 * 
		 * player-12 Bronze-green Gold-health Silver-pr
		 */
	}

	public void analyzeSameResultLastResult()

	{

		// player 1
		check(new String[] { "10-point-green", "bronze-medal-green" }, "1",
				"green leaves");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "king-week-health", "gold-medal-health" },
				"1", "health");
		check(new String[] {}, "1", "p+r");
		check(new String[] {}, "1", "special");

		// player 2
		check(new String[] { "10-point-green", "50-point-green",
				"100-point-green", "gold-medal-green", "king-week-green" },
				"2", "green leaves");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health" }, "2", "health");
		check(new String[] { "10-point-pr", "king-week-pr", "gold-medal-pr" },
				"2", "p+r");
		check(new String[] {}, "2", "special");

		// player 11
		check(new String[] { "10-point-green", "50-point-green",
				"100-point-green", "gold-medal-green", "king-week-green" },
				"11", "green leaves");
		check(new String[] {}, "11", "health");
		check(new String[] {}, "11", "p+r");
		check(new String[] {}, "11", "special");

		// player 12
		check(new String[] { "10-point-green", "bronze-medal-green" }, "12",
				"green leaves");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "king-week-health", "gold-medal-health" },
				"12", "health");
		check(new String[] { "10-point-pr", "silver-medal-pr" }, "12", "p+r");
		check(new String[] {}, "12", "special");

		// player 2442
		check(new String[] { "10-point-green", "bronze-medal-green" }, "2442",
				"green leaves");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "king-week-health", "gold-medal-health" },
				"2442", "health");
		check(new String[] { "10-point-pr", "silver-medal-pr" }, "2442", "p+r");
		check(new String[] {}, "2442", "special");

		// player 242
		check(new String[] { "bronze-medal-green" }, "242", "green leaves");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "king-week-health", "gold-medal-health" },
				"242", "health");
		check(new String[] { "10-point-pr", "silver-medal-pr" }, "242", "p+r");
		check(new String[] {}, "242", "special");

		// player 244
		check(new String[] {}, "244", "green leaves");
		check(new String[] { "king-week-health", "10-point-health",
				"25-point-health", "50-point-health", "gold-medal-health" },
				"244", "health");
		check(new String[] { "10-point-pr", "silver-medal-pr" }, "244", "p+r");
		check(new String[] {}, "244", "special");
	}

	private void launchTaskExecution() {

		GameTask t = new ClassificationTask(null, 3, "green leaves",
				"final classification green");
		t.execute((GameContext) provider.getApplicationContext().getBean(
				"gameCtx", GAME, t));

		t = new ClassificationTask(null, 1, "green leaves",
				"week classification green");
		t.execute((GameContext) provider.getApplicationContext().getBean(
				"gameCtx", GAME, t));

		t = new ClassificationTask(null, 1, "health",
				"week classification health");
		t.execute((GameContext) provider.getApplicationContext().getBean(
				"gameCtx", GAME, t));

		t = new ClassificationTask(null, 1, "p+r", "week classification p+r");
		t.execute((GameContext) provider.getApplicationContext().getBean(
				"gameCtx", GAME, t));

		// final classification

		t = new ClassificationTask(null, 3, "health",
				"final classification health");
		t.execute((GameContext) provider.getApplicationContext().getBean(
				"gameCtx", GAME, t));

		t = new ClassificationTask(null, 3, "p+r", "final classification p+r");
		t.execute((GameContext) provider.getApplicationContext().getBean(
				"gameCtx", GAME, t));
	}

	public void analyzeSimple() {

		// player 1
		check(new String[] { "silver-medal-green", "10-point-green" }, "1",
				"green leaves");
		check(new String[] { "bronze-medal-pr" }, "1", "p+r");
		check(new String[] {}, "1", "health");
		check(new String[] {}, "1", "special");

		// player 2
		check(new String[] { "bronze-medal-green", "10-point-green" }, "2",
				"green leaves");
		check(new String[] { "10-point-pr", "king-week-pr", "gold-medal-pr" },
				"2", "p+r");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "silver-medal-health" }, "2", "health");
		check(new String[] {}, "2", "special");

		// player 11
		check(new String[] { "gold-medal-green", "10-point-green",
				"50-point-green", "100-point-green", "king-week-green" }, "11",
				"green leaves");
		check(new String[] {}, "11", "p+r");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "king-week-health", "gold-medal-health" },
				"11", "health");
		check(new String[] {}, "11", "special");

		// player 122
		check(new String[] {}, "122", "green leaves");
		check(new String[] { "10-point-pr", "silver-medal-pr" }, "122", "p+r");
		check(new String[] { "bronze-medal-health", "10-point-health" }, "122",
				"health");
		check(new String[] {}, "122", "special");

	}

	private void check(String[] values, String playerId, String conceptName) {
		List<PlayerState> states = playerSrv.loadStates(GAME);
		StateAnalyzer analyzer = new StateAnalyzer(states);
		Assert.assertTrue(String.format("Failure concept %s of  player %s",
				playerId, conceptName),
				new HashSet<String>(Arrays.asList(values)).containsAll(analyzer
						.getBadges(analyzer.findPlayer(playerId), conceptName)));
	}

	public void analyzeSameResult() {

		// player 1
		check(new String[] {}, "1", "green leaves");
		check(new String[] { "king-week-health", "10-point-health",
				"25-point-health", "50-point-health", "gold-medal-health" },
				"1", "health");
		check(new String[] { "bronze-medal-pr" }, "1", "p+r");
		check(new String[] {}, "1", "special");

		// player 12
		check(new String[] { "bronze-medal-green" }, "12", "green leaves");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "king-week-health", "gold-medal-health" },
				"12", "health");
		check(new String[] { "10-point-pr", "silver-medal-pr" }, "12", "p+r");
		check(new String[] {}, "12", "special");

		// player 11
		check(new String[] { "10-point-green", "gold-medal-green",
				"king-week-green" }, "11", "green leaves");
		check(new String[] {}, "11", "health");
		check(new String[] {}, "11", "p+r");
		check(new String[] {}, "11", "special");

		// player 2
		check(new String[] { "10-point-green", "gold-medal-green",
				"king-week-green" }, "2", "green leaves");
		check(new String[] { "10-point-health", "25-point-health",
				"50-point-health", "bronze-medal-health" }, "2", "health");
		check(new String[] { "10-point-pr", "king-week-pr", "gold-medal-pr" },
				"2", "p+r");
		check(new String[] {}, "2", "special");

	}

	class StateAnalyzer {
		private List<PlayerState> s;

		public StateAnalyzer(List<PlayerState> s) {
			this.s = s;

		}

		public double getScore(PlayerState ps, String name) {
			for (GameConcept gc : ps.getState()) {
				if (gc instanceof PointConcept && gc.getName().equals(name)) {
					return ((PointConcept) gc).getScore();
				}
			}

			return 0d;
		}

		public List<String> getBadges(PlayerState ps, String name) {
			for (GameConcept gc : ps.getState()) {
				if (gc instanceof BadgeCollectionConcept
						&& gc.getName().equals(name)) {
					return ((BadgeCollectionConcept) gc).getBadgeEarned();
				}
			}

			return Collections.<String> emptyList();
		}

		public PlayerState findPlayer(String playerId) {
			for (PlayerState ps : s) {
				if (ps.getPlayerId().equals(playerId)) {
					return ps;
				}
			}

			return definePlayerState(playerId, 0d, 0d, 0d).toPlayerState();
		}
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
		BadgeCollectionConcept badge = new BadgeCollectionConcept(
				"green leaves");
		myState.add(badge);
		badge = new BadgeCollectionConcept("health");
		myState.add(badge);
		badge = new BadgeCollectionConcept("p+r");
		myState.add(badge);
		badge = new BadgeCollectionConcept("special");
		myState.add(badge);
		player.setState(myState);

		return new StatePersistence(player);
	}

	private GamePersistence defineGame() {
		Game game = new Game();

		game.setId(GAME);
		game.setName(GAME);

		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		game.getActions().add("classification");

		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(new PointConcept("green leaves"));
		game.getConcepts().add(new PointConcept("health"));
		game.getConcepts().add(new PointConcept("p+r"));
		game.getConcepts().add(new BadgeCollectionConcept("green leaves"));
		game.getConcepts().add(new BadgeCollectionConcept("health"));
		game.getConcepts().add(new BadgeCollectionConcept("p+r"));
		game.getConcepts().add(new BadgeCollectionConcept("special"));

		game.setTasks(new HashSet<GameTask>());

		// final classifications
		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("0 20 * * * *");
		ClassificationTask task1 = new ClassificationTask(schedule, 3,
				"green leaves", "final classification green");
		game.getTasks().add(task1);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task2 = new ClassificationTask(schedule, 3,
				"health", "final classification health");
		game.getTasks().add(task2);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task3 = new ClassificationTask(schedule, 3, "p+r",
				"final classification p+r");
		game.getTasks().add(task3);

		// week classifications // schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task4 = new ClassificationTask(schedule, 1,
				"green leaves", "week classification green");
		game.getTasks().add(task4);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task5 = new ClassificationTask(schedule, 1,
				"health", "week classification health");
		game.getTasks().add(task5);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task6 = new ClassificationTask(schedule, 1, "p+r",
				"week classification p+r");
		game.getTasks().add(task6);

		return new GamePersistence(game);

	}
}
