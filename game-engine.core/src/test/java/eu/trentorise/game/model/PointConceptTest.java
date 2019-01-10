package eu.trentorise.game.model;

import java.util.Date;
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
import eu.trentorise.game.core.config.TestCoreConfiguration;
import eu.trentorise.game.managers.GameWorkflow;
import eu.trentorise.game.model.PointConcept.PeriodInstance;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, TestCoreConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
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

    private static final String DOMAIN = "my-domain";

    private static final DateTime TODAY_AT_MIDNIGHT = DateTime.now().withTimeAtStartOfDay();

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

        PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false, false);

        PointConcept p = (PointConcept) loaded.getState().iterator().next();

        Assert.assertEquals(new Double(35), p.getPeriodCurrentScore(0));
    }

    @Test
    public void persistPointConcept() {
        final String GAME_ID = "TEST_GAME";
        final String PLAYER_ID = "1000";
        PointConcept pc = new PointConcept("testPoint");

        pc.addPeriod("period", new Date(), 1000, 5);
        pc.addPeriod("period1", new Date(), DAY_MILLISEC, 1);

        pc.setScore(20d);
        PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
        ps.getState().add(pc);
        ps = playerSrv.saveState(ps);

        PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false, false);

        PointConcept p = (PointConcept) loaded.getState().iterator().next();

        Assert.assertEquals(20d, p.getScore(), 0);

    }

    @Test
    public void capacitySetted() throws InterruptedException {
        PointConcept pc = new PointConcept("testPoint");

        LocalDate start1 = new LocalDate();
        pc.addPeriod("period1", start1.toDate(), DAY_MILLISEC, 3);
        pc.setScore(1d);

        pc.executionMoment = start1.plusDays(1).toDate().getTime();
        pc.setScore(10d);

        pc.executionMoment = start1.plusDays(2).toDate().getTime();
        pc.setScore(30d);

        Assert.assertEquals(20d, pc.getPeriodInstance("period1", 2).getScore(), 0);

        pc.executionMoment = start1.plusDays(3).toDate().getTime();
        pc.setScore(32d);

        pc.executionMoment = start1.plusDays(4).toDate().getTime();
        pc.setScore(33d);

        pc.executionMoment = start1.plusDays(5).toDate().getTime();
        pc.setScore(35d);

        Assert.assertEquals(0d, pc.getPeriodInstance("period1", 2).getScore(), 0);

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

        PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false, false);

        PointConcept p = (PointConcept) loaded.getState().iterator().next();

        Assert.assertEquals(new Double(21), p.getPeriodCurrentScore(0));
        Assert.assertEquals(new Double(21), p.getPeriodCurrentScore(1));

    }

    private PointConcept setupPointConceptWithPeriods() {
        PointConcept pc = new PointConcept("testPoint");
        LocalDate startPeriodToday = new LocalDate();
        pc.addPeriod("period1", startPeriodToday.minusDays(1).toDate(), DAY_MILLISEC);
        pc.addPeriod("period2", startPeriodToday.toDate(), HOUR_MILLISEC);

        /*
         * period1: today-1 10, today 29
         * 
         * period2: 00 4, 01 10, 02 1, 03 14
         */

        pc.executionMoment = TODAY_AT_MIDNIGHT.minusDays(1).getMillis();
        pc.setScore(10d);

        pc.executionMoment = TODAY_AT_MIDNIGHT.getMillis();
        pc.setScore(14d); // +4

        pc.executionMoment = TODAY_AT_MIDNIGHT.plusHours(1).getMillis();
        pc.setScore(18d); // +4
        pc.setScore(24d); // +6

        pc.executionMoment = TODAY_AT_MIDNIGHT.plusHours(2).getMillis();
        pc.setScore(25d);// +1

        pc.executionMoment = TODAY_AT_MIDNIGHT.plusHours(3).getMillis();
        pc.setScore(39d); // +14

        return pc;
    }

    @Test
    public void currentInstanceScore() {
        PointConcept p = setupPointConceptWithPeriods();

        Assert.assertEquals(new Double(29), p.getPeriodCurrentScore("period1"));
        Assert.assertEquals(new Double(14), p.getPeriodCurrentScore("period2"));
    }

    @Test
    public void previousInstanceScore() {
        PointConcept p = setupPointConceptWithPeriods();

        Assert.assertEquals(new Double(10), p.getPeriodPreviousScore("period1"));
        Assert.assertEquals(new Double(1), p.getPeriodPreviousScore("period2"));
    }

    @Test
    public void previousInstanceScoreOfNonExistingPeriod() {
        PointConcept p = setupPointConceptWithPeriods();

        Assert.assertEquals(new Double(0), p.getPeriodPreviousScore("period45"));
    }

    @Test
    public void instanceScoreByIndex() {
        PointConcept p = setupPointConceptWithPeriods();

        Assert.assertEquals(new Double(10), p.getPeriodScore("period1", 0));
        Assert.assertEquals(new Double(4), p.getPeriodScore("period2", 0));
        Assert.assertEquals(new Double(10), p.getPeriodScore("period2", 1));
        Assert.assertEquals(new Double(1), p.getPeriodScore("period2", 2));
    }

    @Test
    public void instanceScoreByDate() {
        PointConcept p = setupPointConceptWithPeriods();

        Assert.assertEquals(new Double(1),
                p.getPeriodScore("period2", TODAY_AT_MIDNIGHT.plusHours(2).getMillis()));
        Assert.assertEquals(new Double(4),
                p.getPeriodScore("period2", TODAY_AT_MIDNIGHT.getMillis()));
    }

    @Test
    public void instanceScoreByFutureDate() {
        PointConcept p = setupPointConceptWithPeriods();

        Assert.assertEquals(new Double(0),
                p.getPeriodScore("period2", TODAY_AT_MIDNIGHT.plusDays(1).getMillis()));
    }

    @Test
    public void instanceScoreByNonExistingIndex() {
        PointConcept p = setupPointConceptWithPeriods();

        Assert.assertEquals(new Double(0), p.getPeriodScore("period2", 10));
    }

    @Test
    public void currentInstance() {
        PointConcept p = setupPointConceptWithPeriods();

        PeriodInstance current = p.getPeriodCurrentInstance("period2");
        Assert.assertEquals(Double.valueOf(14), current.getScore());

        Assert.assertEquals(TODAY_AT_MIDNIGHT.plusHours(3).getMillis(), current.getStart());
        Assert.assertEquals(TODAY_AT_MIDNIGHT.plusHours(3).getMillis() + HOUR_MILLISEC,
                current.getEnd());
    }

    @Test
    public void instanceByIndex() {
        PointConcept p = setupPointConceptWithPeriods();

        PeriodInstance instance = p.getPeriodInstance("period2", 0);
        Assert.assertEquals(Double.valueOf(4), instance.getScore());
        Assert.assertEquals(TODAY_AT_MIDNIGHT.getMillis(), instance.getStart());
        Assert.assertEquals(TODAY_AT_MIDNIGHT.getMillis() + HOUR_MILLISEC, instance.getEnd());

        instance = p.getPeriodInstance("period2", 2);
        Assert.assertEquals(new Double(1), instance.getScore());
        Assert.assertEquals(TODAY_AT_MIDNIGHT.plusHours(2).getMillis(), instance.getStart());
        Assert.assertEquals(TODAY_AT_MIDNIGHT.plusHours(2).getMillis() + HOUR_MILLISEC,
                instance.getEnd());
    }

    @Test
    public void instanceByNonExistingIndex() {
        PointConcept p = setupPointConceptWithPeriods();
        Assert.assertEquals(new Double(0), p.getPeriodInstance("period2", 20).getScore());
    }

    @Test
    public void previousInstanceScoreByFutureDate() {
        PointConcept p = setupPointConceptWithPeriods();

        // move to future date
        p.executionMoment = TODAY_AT_MIDNIGHT.plusDays(5).getMillis();

        Assert.assertEquals(new Double(0), p.getPeriodPreviousScore("period1"));
        Assert.assertEquals(new Double(0), p.getPeriodPreviousInstance("period1").getScore());
    }

    @Test
    public void previousInstanceByFutureDate() {
        PointConcept p = setupPointConceptWithPeriods();

        // move to future date
        p.executionMoment = TODAY_AT_MIDNIGHT.plusDays(5).getMillis();

        PeriodInstance instance = p.getPeriodPreviousInstance("period1");
        Assert.assertEquals(new Double(0), instance.getScore());
    }

    /*
     * Maintain this test to check behavior on pointconcept when its loaded by db.
     * 
     * This test is not pure unitary, think about to remove it
     */
    @Test
    public void differentInstances() {
        final String GAME_ID = "TEST_GAME";
        final String PLAYER_ID = "1000";

        PointConcept pc = setupPointConceptWithPeriods();

        PlayerState ps = new PlayerState(GAME_ID, PLAYER_ID);
        ps.getState().add(pc);
        ps = playerSrv.saveState(ps);

        PlayerState loaded = playerSrv.loadState(GAME_ID, PLAYER_ID, false, false);

        PointConcept p = (PointConcept) loaded.getState().iterator().next();
        p.executionMoment = pc.executionMoment;

        Assert.assertEquals(new Double(29), p.getPeriodCurrentScore("period1"));
        Assert.assertEquals(new Double(10), p.getPeriodPreviousScore("period1"));
        Assert.assertEquals(new Double(0), p.getPeriodPreviousScore("period45"));
        Assert.assertEquals(new Double(10), p.getPeriodScore("period1", 0));
        Assert.assertEquals(new Double(1),
                p.getPeriodScore("period2", TODAY_AT_MIDNIGHT.plusHours(2).getMillis()));

        Assert.assertEquals(new Double(0),
                p.getPeriodScore("period2", TODAY_AT_MIDNIGHT.plusDays(1).getMillis()));
    }

    /*
     * Test issue of daylight saving time
     */
    @Test
    public void dstPeriodStartInExecMomentOut() {
        final LocalDate IN_DST_DATE = new LocalDate(2016, 10, 22);

        final long WEEKLY_PERIOD_TS = 604800000;

        PointConcept p = new PointConcept("green", new DateTime(2016, 12, 1, 10, 1).getMillis());

        p.addPeriod("dstPeriod", IN_DST_DATE.toDate(), WEEKLY_PERIOD_TS);

        PeriodInstance instance = p.getPeriodCurrentInstance("dstPeriod");
        Assert.assertEquals(new DateTime(2016, 11, 26, 0, 0), new DateTime(instance.getStart()));
    }

    @Test
    public void dstPeriodStartOutExecMomentOut() {
        final LocalDate OUT_DST_DATE = new LocalDate(2016, 11, 5);

        final long WEEKLY_PERIOD_TS = 604800000;

        PointConcept p = new PointConcept("green", new DateTime(2016, 12, 1, 10, 1).getMillis());

        p.addPeriod("noDSTPeriod", OUT_DST_DATE.toDate(), WEEKLY_PERIOD_TS);

        PeriodInstance instance = p.getPeriodCurrentInstance("noDSTPeriod");
        Assert.assertEquals(new DateTime(2016, 11, 26, 0, 0), new DateTime(instance.getStart()));
    }

    @Test
    public void dstPeriodStartOutExecMomentIn() {
        final LocalDate OUT_DST_DATE = new LocalDate(2016, 11, 5);

        final long WEEKLY_PERIOD_TS = 604800000;

        PointConcept p = new PointConcept("green", new DateTime(2017, 4, 25, 10, 1).getMillis());

        p.addPeriod("noDSTPeriod", OUT_DST_DATE.toDate(), WEEKLY_PERIOD_TS);

        PeriodInstance instance = p.getPeriodCurrentInstance("noDSTPeriod");
        Assert.assertEquals(new DateTime(2017, 4, 22, 0, 0), new DateTime(instance.getStart()));
    }

    @Test
    public void dstPeriodStartInExecMomentIn() {
        final LocalDate OUT_DST_DATE = new LocalDate(2016, 10, 22);

        final long WEEKLY_PERIOD_TS = 604800000;

        PointConcept p = new PointConcept("green", new DateTime(2017, 4, 25, 10, 1).getMillis());

        p.addPeriod("noDSTPeriod", OUT_DST_DATE.toDate(), WEEKLY_PERIOD_TS);

        PeriodInstance instance = p.getPeriodCurrentInstance("noDSTPeriod");
        Assert.assertEquals(new DateTime(2017, 4, 22, 0, 0), new DateTime(instance.getStart()));
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
        game.setDomain(DOMAIN);
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
        // Thread.sleep(30000);

        Assert.assertEquals(new Double(4), ((PointConcept) playerSrv
                .loadState(GAME, "my player", false, false).getState().iterator().next()).getScore());

    }
}
