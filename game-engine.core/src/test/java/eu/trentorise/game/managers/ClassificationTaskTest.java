package eu.trentorise.game.managers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
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
import eu.trentorise.game.core.config.TestCoreConfiguration;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.model.core.TimeUnit;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.task.IncrementalClassificationTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, TestCoreConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
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

    @Autowired
    private AppContextProvider provider;

    // private static final long WAIT_EXEC = 15 * 1000;

    private static final String DOMAIN = "my-domain";

    @Before
    public void cleanDB() {
        // clean mongo
        mongo.getDb().dropDatabase();
    }

    @After
    public void after() {
        cleanDB();
    }

    @Test
    public void incremental() {
        Game g = new Game();
        g.setId("test");
        g.setDomain(DOMAIN);

        PointConcept p = new PointConcept("yellow");
        final long EVERY_MINUTE = 60000;
        p.addPeriod("period1", new LocalDate().toDate(), EVERY_MINUTE);
        g.setConcepts(new HashSet<GameConcept>());
        g.getConcepts().add(p);

        g.setTasks(new HashSet<GameTask>());
        IncrementalClassificationTask incClass =
                new IncrementalClassificationTask(p, "period1", "final classification green");
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
    public void incrementalWithDelay() {
        Game g = new Game();
        g.setId("test");
        g.setDomain(DOMAIN);

        PointConcept p = new PointConcept("yellow");
        final long EVERY_MINUTE = 60000;
        p.addPeriod("period1", new LocalDate().toDate(), EVERY_MINUTE);
        g.setConcepts(new HashSet<GameConcept>());
        g.getConcepts().add(p);

        g.setTasks(new HashSet<GameTask>());
        IncrementalClassificationTask incClass = new IncrementalClassificationTask(p, "period1",
                "final classification green", new TimeInterval(2, TimeUnit.MINUTE));
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
        game.setDomain(DOMAIN);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);

        PointConcept green = new PointConcept("green leaves");
        green.addPeriod("important", new LocalDate().toDate(), 1000l);

        game.setConcepts(new HashSet<GameConcept>());
        game.getConcepts().add(new PointConcept("green leaves"));
        game.getConcepts().add(new PointConcept("health"));
        game.getConcepts().add(new BadgeCollectionConcept("green leaves"));

        game.setTasks(new HashSet<GameTask>());

        IncrementalClassificationTask task =
                new IncrementalClassificationTask(green, "important", "final classification green");

        game.getTasks().add(task);

        gameSrv.saveGameDefinition(game);

        // add rules
        ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME + "/constants");
        rule.setName("constants");
        gameSrv.addRule(rule);
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenBadges.drl"));
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/healthPoints.drl"));
        gameSrv.addRule(
                new ClasspathRule(GAME, "rules/" + GAME + "/finalClassificationBadges.drl"));

        PlayerState p = playerSrv.loadState(GAME, PLAYER, true, false);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bikeDistance", 8.43);
        params.put("walkDistance", 3.100);
        params.put("bikesharing", true);
        params.put("sustainable", true);
        params.put("p+r", true);
        params.put("park", "MANIFATTURA");
        p = engine.execute(GAME, p, ACTION, params, UUID.randomUUID().toString(),
                System.currentTimeMillis(), null);
        // Thread.sleep(WAIT_EXEC);
        // expected 60 greenPoints and earned 10-point 50-point green badges
        boolean found = false;
        for (GameConcept gc : p.getState()) {
            if (gc instanceof PointConcept && gc.getName().equals("green leaves")) {
                found = true;
                Assert.assertEquals(70d, ((PointConcept) gc).getScore().doubleValue(), 0);
            }
            if (gc instanceof BadgeCollectionConcept && gc.getName().equals("green leaves")) {
                found = found && true;
                Assert.assertArrayEquals(new String[] {"10-point-green", "50-point-green"},
                        ((BadgeCollectionConcept) gc).getBadgeEarned().toArray(new String[1]));
            }

        }
        if (!found) {
            Assert.fail("gameconcepts not found");
        }
    }

    /*
     * I want to test a scenario with some execution sequences, but cause to executionMoment in
     * PointConcept, I'm able to test only actual execution
     */
    @Test
    public void incrementalWithRule() throws InterruptedException {
        cleanDB();
        final String GAME = "classification";
        final String OWNER = "testOwner";
        final String ACTION = "myAction";
        final String PLAYER_1 = "eddie brock";
        final String PLAYER_2 = "mac gargan";
        final String PLAYER_3 = "jonah jameson";
        final long PERIOD_LENGTH = 24 * 3600000l; // one day

        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);
        game.setOwner(OWNER);
        game.setDomain(DOMAIN);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);

        PointConcept green = new PointConcept("green leaves");
        green.addPeriod("important", new LocalDate().minusDays(2).toDate(), PERIOD_LENGTH);

        game.setConcepts(new HashSet<GameConcept>());
        game.getConcepts().add(green);
        game.getConcepts().add(new BadgeCollectionConcept("green leaves"));

        game.setTasks(new HashSet<GameTask>());

        IncrementalClassificationTask task =
                new IncrementalClassificationTask(green, "important", "final classification green");

        game.getTasks().add(task);

        gameSrv.saveGameDefinition(game);

        // add rules
        ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME + "/constants");
        rule.setName("constants");
        gameSrv.addRule(rule);
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenBadges.drl"));
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));
        gameSrv.addRule(
                new ClasspathRule(GAME, "rules/" + GAME + "/finalClassificationBadges.drl"));

        // define a dataset of player states
        DateTime timeMachine = new DateTime().withTimeAtStartOfDay().plusMinutes(1).minusDays(2);

        // today -2
        PlayerState p1 = new PlayerState(GAME, PLAYER_1);
        p1.setState(new HashSet<GameConcept>());
        PointConcept g = new PointConcept("green leaves");
        g.addPeriod("important", new LocalDate().minusDays(2).toDate(), PERIOD_LENGTH);
        g.setExecutionMoment(timeMachine.getMillis());
        g.increment(5d);
        p1.getState().add(g);
        playerSrv.saveState(p1);
        PlayerState p2 = new PlayerState(GAME, PLAYER_2);
        p2.setState(new HashSet<GameConcept>());
        PointConcept g1 = new PointConcept("green leaves");
        g1.addPeriod("important", new LocalDate().minusDays(2).toDate(), PERIOD_LENGTH);
        g1.setExecutionMoment(timeMachine.getMillis());
        g1.increment(7d);
        p2.getState().add(g1);
        playerSrv.saveState(p2);
        PlayerState p3 = new PlayerState(GAME, PLAYER_3);
        p3.setState(new HashSet<GameConcept>());
        PointConcept g2 = new PointConcept("green leaves");
        g2.addPeriod("important", new LocalDate().minusDays(2).toDate(), PERIOD_LENGTH);
        g2.setExecutionMoment(timeMachine.getMillis());
        g2.increment(3d);
        p3.getState().add(g2);
        playerSrv.saveState(p3);

        // today -1
        g.setExecutionMoment(timeMachine.plusDays(1).getMillis());
        g.increment(1d);
        playerSrv.saveState(p1);
        g1.setExecutionMoment(timeMachine.plusDays(1).getMillis());
        g1.increment(9d);
        playerSrv.saveState(p2);
        g2.setExecutionMoment(timeMachine.plusDays(1).getMillis());
        g2.increment(3d);
        playerSrv.saveState(p3);

        // today
        g.setExecutionMoment(timeMachine.plusDays(2).getMillis());
        g.increment(2d);
        playerSrv.saveState(p1);
        g1.setExecutionMoment(timeMachine.plusDays(2).getMillis());
        g1.increment(10d);
        playerSrv.saveState(p2);
        g2.setExecutionMoment(timeMachine.plusDays(2).getMillis());
        g2.increment(31d);
        playerSrv.saveState(p3);

        task.execute((GameContext) provider.getApplicationContext().getBean("gameCtx", GAME, task));

        // task.execute((GameContext) provider.getApplicationContext().getBean(
        // "gameCtx", GAME, task));

        // task.execute((GameContext) provider.getApplicationContext().getBean(
        // "gameCtx", GAME, task));

        // Thread.sleep(WAIT_EXEC);

        p1 = playerSrv.loadState(GAME, PLAYER_1, false, false);
        for (GameConcept gc : p1.getState()) {
            if (gc instanceof BadgeCollectionConcept && gc.getName().equals("green leaves")) {
                Assert.assertArrayEquals(new String[] {"bronze-medal-green"},
                        ((BadgeCollectionConcept) gc).getBadgeEarned().toArray(new String[1]));
                break;
            }
        }

        p2 = playerSrv.loadState(GAME, PLAYER_2, false, false);
        for (GameConcept gc : p2.getState()) {
            if (gc instanceof BadgeCollectionConcept && gc.getName().equals("green leaves")) {
                Assert.assertArrayEquals(new String[] {"silver-medal-green", "10-point-green"},
                        ((BadgeCollectionConcept) gc).getBadgeEarned().toArray(new String[1]));
                break;
            }
        }

        p3 = playerSrv.loadState(GAME, PLAYER_3, false, false);
        for (GameConcept gc : p3.getState()) {
            if (gc instanceof BadgeCollectionConcept && gc.getName().equals("green leaves")) {
                Assert.assertArrayEquals(new String[] {"gold-medal-green-1", "10-point-green"},
                        ((BadgeCollectionConcept) gc).getBadgeEarned().toArray(new String[1]));
                break;
            }
        }
    }

    @Test
    public void create_incremental_using_a_period_already_start() {
        PointConcept score = new PointConcept("green");
        Date today = LocalDate.now().toDate();
        long oneDay = 86400000;
        score.addPeriod("my-period", today, oneDay);
        IncrementalClassificationTask task =
                new IncrementalClassificationTask(score, "my-period", "classification");

        Assert.assertEquals(today.getTime(), task.getStartupPeriodInstance());
        Assert.assertEquals(0, task.getStartupInstanceIndex());
        Date tomorrow = LocalDate.now().plusDays(1).toDate();
        Assert.assertEquals(tomorrow, task.getSchedule().getStart());
    }

    @Test
    public void create_incremental_using_a_period_starting_in_future() {
        PointConcept score = new PointConcept("green");
        Date tomorrow = LocalDate.now().plusDays(1).toDate();
        long oneDay = 86400000;
        score.addPeriod("my-period", tomorrow, oneDay);
        IncrementalClassificationTask task =
                new IncrementalClassificationTask(score, "my-period", "classification");

        Assert.assertEquals(tomorrow.getTime(), task.getStartupPeriodInstance());
        Assert.assertEquals(0, task.getStartupInstanceIndex());
        Date sinceTwoDays = LocalDate.now().plusDays(2).toDate();
        Assert.assertEquals(sinceTwoDays, task.getSchedule().getStart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_incremental_using_non_existent() {
        PointConcept score = new PointConcept("green");
        IncrementalClassificationTask task =
                new IncrementalClassificationTask(score, "my-period", "classification");
        
    }
}
