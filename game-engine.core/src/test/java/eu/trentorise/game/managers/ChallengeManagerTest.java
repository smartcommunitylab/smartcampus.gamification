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
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.Clock;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
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

    @InjectMocks
    @Autowired
    private ChallengeManager challengeManager;


    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Mock
    private Clock clock;

    @Before
    public void setup() {
        mongo.getDb().dropDatabase();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void no_winners() {
        GroupChallenge groupChallenge = new GroupChallenge();
        assertThat(challengeManager.conditionCheck(groupChallenge), is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
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

        challengeManager.conditionCheck(groupChallenge);
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

    @Test
    public void load_completed_performance() {
        BDDMockito.given(clock.now()).willReturn(date("2018-09-27T17:00:00"));

        GroupChallenge assign = new GroupChallenge();
        assign.setGameId("game");
        assign.setEnd(date("2018-09-27T00:00:00"));
        challengeManager.save(assign);

        GroupChallenge assign1 = new GroupChallenge();
        assign1.setGameId("game");
        assign1.setEnd(date("2018-09-29T09:00:00"));
        challengeManager.save(assign1);

        List<GroupChallenge> completedGroupChallenges =
                challengeManager.completedPerformanceGroupChallenges("game");
        assertThat(completedGroupChallenges, hasSize(1));
    }

    @Test
    public void load_two_perfomance_challenges() {
        BDDMockito.given(clock.now()).willReturn(date("2018-09-29T09:00:00"));

        GroupChallenge challenge1 = new GroupChallenge();
        challenge1.setGameId("game");
        challenge1.setEnd(date("2018-09-27T00:00:00"));
        challenge1.setState(ChallengeState.PROPOSED);
        challengeManager.save(challenge1);

        GroupChallenge challenge2 = new GroupChallenge();
        challenge2.setGameId("game");
        challenge2.setEnd(date("2018-09-30T09:00:00"));
        challengeManager.save(challenge2);

        GroupChallenge challenge3 = new GroupChallenge();
        challenge3.setGameId("other-game");
        challenge3.setEnd(date("2018-09-17T09:00:00"));
        challengeManager.save(challenge3);

        GroupChallenge challenge4 = new GroupChallenge();
        challenge4.setGameId("game");
        challenge4.setEnd(date("2018-09-17T09:00:00"));
        challengeManager.save(challenge4);

        GroupChallenge challenge5 = new GroupChallenge();
        challenge5.setGameId("game");
        challenge5.setEnd(date("2018-09-29T08:00:00"));
        challengeManager.save(challenge5);

        List<GroupChallenge> completedGroupChallenges =
                challengeManager.completedPerformanceGroupChallenges("game");
        assertThat(completedGroupChallenges, hasSize(2));
    }
}
