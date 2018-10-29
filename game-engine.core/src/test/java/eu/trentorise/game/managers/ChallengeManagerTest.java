package eu.trentorise.game.managers;

import static eu.trentorise.game.test_utils.Utils.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.Clock;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeInvitation;
import eu.trentorise.game.model.ChallengeInvitation.Player;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.repo.ChallengeModelRepo;
import eu.trentorise.game.repo.GroupChallengeRepo;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class ChallengeManagerTest {

    @InjectMocks
    @Autowired
    private ChallengeManager challengeManager;

    @Autowired
    private GroupChallengeRepo groupChallengeRepo;

    @InjectMocks
    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Mock
    private Clock clock;

    @Mock
    private GameService gameSrv;

    @Mock
    private ChallengeModelRepo challengeModelRepo;

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

    @Test
    public void ant_man_invites_wasp_to_challenge() {
        BDDMockito.given(gameSrv.loadGameDefinitionById("GAME")).willReturn(new Game("GAME"));

        ChallengeInvitation invitation =
                invitation("GAME", "ant-man", "wasp", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(invitation);

        assertThat(groupChallengeRepo.proposerInvitations("GAME", "ant-man"), hasSize(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ant_man_already_invite_to_challenge() {
        BDDMockito.given(gameSrv.loadGameDefinitionById("GAME")).willReturn(new Game("GAME"));

        ChallengeInvitation invitation =
                invitation("GAME", "ant-man", "wasp", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(invitation);
        assertThat(groupChallengeRepo.proposerInvitations("GAME", "ant-man"), hasSize(1));

        ChallengeInvitation otherInvitation =
                invitation("GAME", "ant-man", "jessica-jones", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(otherInvitation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ant_man_invites_wasp_but_she_has_already_reach_limit_invitations() {
        BDDMockito.given(gameSrv.loadGameDefinitionById("GAME")).willReturn(new Game("GAME"));
        ChallengeInvitation drStrangeInvitation =
                invitation("GAME", "dr. strange", "wasp", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(drStrangeInvitation);

        ChallengeInvitation connorsInvitation =
                invitation("GAME", "curt connors", "wasp", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(connorsInvitation);

        ChallengeInvitation brockInvitation =
                invitation("GAME", "eddie brock", "wasp", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(brockInvitation);

        ChallengeInvitation antManInvitation =
                invitation("GAME", "ant-man", "wasp", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(antManInvitation);
        Assert.fail("ant-man invitation works and it should not");
    }


    @Test
    public void wasp_accept_invitation() {
        BDDMockito.given(gameSrv.loadGameDefinitionById("GAME")).willReturn(new Game("GAME"));
        ChallengeInvitation drStrangeInvitation =
                invitation("GAME", "dr. strange", "wasp", "groupCompetitivePerformance");
        GroupChallenge invitationChallenge =
                challengeManager.inviteToChallenge(drStrangeInvitation);
        assertThat(invitationChallenge.getState(), is(ChallengeState.PROPOSED));
        GroupChallenge invitationAccepted = challengeManager.acceptInvitation("GAME", "wasp",
                invitationChallenge.getInstanceName());
        assertThat(invitationAccepted.getState(), is(ChallengeState.ASSIGNED));
    }

    @Test
    public void wasp_accept_non_existentinvitation() {
        BDDMockito.given(gameSrv.loadGameDefinitionById("GAME")).willReturn(new Game("GAME"));
        ChallengeInvitation drStrangeInvitation =
                invitation("GAME", "dr. strange", "wasp", "groupCompetitivePerformance");
        GroupChallenge invitationChallenge =
                challengeManager.inviteToChallenge(drStrangeInvitation);
        assertThat(invitationChallenge.getState(), is(ChallengeState.PROPOSED));
        GroupChallenge invitationAccepted =
                challengeManager.acceptInvitation("GAME", "wasp", "nonExistentInvitation");
        assertThat(invitationAccepted, is(nullValue()));
    }

    @Test
    public void wasp_accept_invitation_having_other_proposed_challenges() {
        BDDMockito.given(gameSrv.loadGameDefinitionById("GAME")).willReturn(new Game("GAME"));
        BDDMockito.given(challengeModelRepo.findByGameIdAndName("GAME","model_1")).will(new Answer<ChallengeModel>() {

            @Override
            public ChallengeModel answer(InvocationOnMock arg0) throws Throwable {
                ChallengeModel model = new ChallengeModel();
                model.setName("model_1");
                return model;
            }
                });

        ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                new HashMap<>(), "PROPOSED", null, null);
        playerSrv.assignChallenge("GAME", "wasp", assignment);

        ChallengeAssignment assignment1 = new ChallengeAssignment("model_1", "instance_name1",
                new HashMap<>(), "PROPOSED", null, null);
        playerSrv.assignChallenge("GAME", "wasp", assignment1);

        ChallengeInvitation antManInvitation =
                invitation("GAME", "ant-man", "wasp", "groupCompetitivePerformance");
        challengeManager.inviteToChallenge(antManInvitation);
        ChallengeInvitation drStrangeInvitation =
                invitation("GAME", "dr. strange", "wasp", "groupCompetitivePerformance");
        GroupChallenge invitationChallenge =
                challengeManager.inviteToChallenge(drStrangeInvitation);
        assertThat(invitationChallenge.getState(), is(ChallengeState.PROPOSED));
        GroupChallenge invitationAccepted = challengeManager.acceptInvitation("GAME", "wasp",
                invitationChallenge.getInstanceName());
        assertThat(invitationAccepted.getState(), is(ChallengeState.ASSIGNED));
        PlayerState waspState = playerSrv.loadState("GAME", "wasp", false, true);
        long proposedCount = waspState.challenges().stream()
                .filter(c -> c.getState() == ChallengeState.PROPOSED).count();
        long assignedCount = waspState.challenges().stream()
                .filter(c -> c.getState() == ChallengeState.ASSIGNED).count();
        assertThat(assignedCount, is(1L));
        assertThat(proposedCount, is(0L));
    }


    private ChallengeInvitation invitation(String gameId, String proposerId, String guestId, String type) {
        ChallengeInvitation invitation = new ChallengeInvitation();
        invitation.setGameId(gameId);
        invitation.setChallengeModelName(type);
        Player proposer = new Player();
        proposer.setPlayerId(proposerId);
        invitation.setProposer(proposer);
        Player guest = new Player();
        guest.setPlayerId(guestId);
        invitation.getGuests().add(guest);
        return invitation;
    }
}
