package eu.trentorise.game.managers;

import static eu.trentorise.game.test_utils.Utils.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
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
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class ChallengeManagerTest {

    @Autowired
    private ChallengeManager challengeManager;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Before
    public void setup() {
        // clean mongo
        mongo.getDb().dropDatabase();
    }

    @Test
    public void no_winners() {
        GroupChallenge groupChallenge = new GroupChallenge();
        assertThat(challengeManager.conditionCheck(groupChallenge), is(empty()));
    }

    @Test
    public void players_not_exist() {
        GroupChallenge groupChallenge = new GroupChallenge();
        Attendee antMan = new Attendee();
        antMan.setRole(Role.GUEST);
        antMan.setPlayerId("Ant-man");
        groupChallenge.getAttendees().add(antMan);

        Attendee wasp = new Attendee();
        wasp.setRole(Role.GUEST);
        wasp.setPlayerId("Wasp");
        groupChallenge.getAttendees().add(wasp);

        assertThat(challengeManager.conditionCheck(groupChallenge), is(empty()));
    }

    @Test
    public void wasp_wins() {
        Date startDate = date("2018-09-26T00:00:00");
        long executionMoment = new DateTime(startDate.getTime()).plusHours(5).getMillis();

        PointConcept antManWalkKmScore = new PointConcept("Walk_Km", executionMoment);
        long ONE_DAY_MILLIS = 86400000;
        antManWalkKmScore.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
        antManWalkKmScore.setScore(5d);

        PlayerState antManState = new PlayerState("game", "Ant-man");
        antManState.getState().add(antManWalkKmScore);
        playerSrv.saveState(antManState);


        executionMoment = new DateTime(startDate.getTime()).plusHours(2).getMillis();
        PointConcept waspWalkKmScore = new PointConcept("Walk_Km", executionMoment);
        waspWalkKmScore.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
        waspWalkKmScore.setScore(7d);

        PlayerState waspState = new PlayerState("game", "Wasp");
        waspState.getState().add(waspWalkKmScore);
        playerSrv.saveState(waspState);

        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setGameId("game");
        Attendee antMan = new Attendee();
        antMan.setRole(Role.GUEST);
        antMan.setPlayerId("Ant-man");
        groupChallenge.getAttendees().add(antMan);

        Attendee wasp = new Attendee();
        wasp.setRole(Role.GUEST);
        wasp.setPlayerId("Wasp");
        groupChallenge.getAttendees().add(wasp);

        PointConceptRef challengePointConcept = new PointConceptRef("Walk_Km", "weekly");
        groupChallenge.setChallengePointConcept(challengePointConcept);

        groupChallenge.setStart(startDate);
        groupChallenge.setEnd(new DateTime(startDate.getTime()).plusDays(1).toDate());


        assertThat(challengeManager.conditionCheck(groupChallenge), hasSize(1));
        assertThat(challengeManager.conditionCheck(groupChallenge), contains("Wasp"));
    }


    @Test
    public void tie() {
        Date startDate = date("2018-09-26T00:00:00");
        long executionMoment = new DateTime(startDate.getTime()).plusHours(5).getMillis();

        PointConcept antManWalkKmScore = new PointConcept("Walk_Km", executionMoment);
        long ONE_DAY_MILLIS = 86400000;
        antManWalkKmScore.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
        antManWalkKmScore.setScore(7d);

        PlayerState antManState = new PlayerState("game", "Ant-man");
        antManState.getState().add(antManWalkKmScore);
        playerSrv.saveState(antManState);


        executionMoment = new DateTime(startDate.getTime()).plusHours(2).getMillis();
        PointConcept waspWalkKmScore = new PointConcept("Walk_Km", executionMoment);
        waspWalkKmScore.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
        waspWalkKmScore.setScore(7d);

        PlayerState waspState = new PlayerState("game", "Wasp");
        waspState.getState().add(waspWalkKmScore);
        playerSrv.saveState(waspState);

        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setGameId("game");
        Attendee antMan = new Attendee();
        antMan.setRole(Role.GUEST);
        antMan.setPlayerId("Ant-man");
        groupChallenge.getAttendees().add(antMan);

        Attendee wasp = new Attendee();
        wasp.setRole(Role.GUEST);
        wasp.setPlayerId("Wasp");
        groupChallenge.getAttendees().add(wasp);

        PointConceptRef challengePointConcept = new PointConceptRef("Walk_Km", "weekly");
        groupChallenge.setChallengePointConcept(challengePointConcept);

        groupChallenge.setStart(startDate);
        groupChallenge.setEnd(new DateTime(startDate.getTime()).plusDays(1).toDate());

        final List<String> winners = challengeManager.conditionCheck(groupChallenge);

        assertThat(winners, hasSize(2));
        assertThat(winners, containsInAnyOrder("Wasp", "Ant-man"));
    }
}
