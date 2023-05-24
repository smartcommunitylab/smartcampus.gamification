package eu.trentorise.game.managers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.RabbitConf;
import eu.trentorise.game.core.Clock;
import eu.trentorise.game.core.ExecutionClock;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.model.core.TimeUnit;
import eu.trentorise.game.repo.GroupChallengeRepo;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, RabbitConf.class, BraveAutoConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
public class ChallengeTest {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private ChallengeManager challengeSrv;

    @Autowired
    private GroupChallengeRepo groupChallengeRepo;

    @Autowired
    private GameEngine engine;

    @Autowired
    private MongoTemplate mongo;

    @Mock
    private Clock clock;

    private static final String GAME = "challengeGameTest";
    private static final String PLAYER = "eddie brock";
    private static final String ACTION = "beatme";
    private static final String DOMAIN = "my-domain";

    @Before
    public void setup() {
        // clean mongo
        mongo.getDb().drop();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void challengeCompleted() {
        ChallengeConcept challenge = new ChallengeConcept();
        Assert.assertEquals(false, challenge.isCompleted());
        Assert.assertNull(challenge.getDateCompleted());
        challenge.completed();
        Assert.assertEquals(true, challenge.isCompleted());
        Assert.assertNotNull(challenge.getDateCompleted());
    }

    @Test
    public void loadChallenges() {
        Game game = gameSrv.saveGameDefinition(defineGame());

        // add rules
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenges.drl"));
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/itinery.drl"));

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);



        LocalDate today = new LocalDate();
        ChallengeAssignment assignment = new ChallengeAssignment("prize", null, null, null,
                today.toDate(), today.dayOfMonth().addToCopy(2).toDate());
        playerSrv.assignChallenge(GAME, PLAYER, assignment);

        PlayerState p = playerSrv.loadState(GAME, PLAYER, false, false);

        // execution
        p = engine.execute(GAME, p, ACTION, null, UUID.randomUUID().toString(),
                System.currentTimeMillis(), null);

        Assert.assertEquals(2, p.getState().size());
        ChallengeConcept ch = null;
        for (GameConcept gc : p.getState()) {
            if (gc instanceof ChallengeConcept) {
                ch = (ChallengeConcept) gc;
            }
        }

        Assert.assertTrue(ch.isCompleted());
    }

    @Test
    public void endedChallenges() {
        Game game = gameSrv.saveGameDefinition(defineGame());

        // add rules
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenges.drl"));
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/itinery.drl"));

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);

        LocalDate today = new LocalDate();

        playerSrv.assignChallenge(GAME, PLAYER,
                new ChallengeAssignment("prize", null, null, null,
                        today.dayOfMonth().addToCopy(-2).toDate(),
                        today.dayOfMonth().addToCopy(-1).toDate()));

        playerSrv.assignChallenge(GAME, PLAYER,
                new ChallengeAssignment("prize", null, null, null,
                        today.dayOfMonth().addToCopy(5).toDate(),
                        today.dayOfMonth().addToCopy(7).toDate()));

        PlayerState p = playerSrv.loadState(GAME, PLAYER, false, false);

        // execution
        p = engine.execute(GAME, p, ACTION, null, UUID.randomUUID().toString(),
                DateTime.now().getMillis(), null);

        Assert.assertEquals(3, p.getState().size());
        ChallengeConcept ch = null;
        for (GameConcept gc : p.getState()) {
            if (gc instanceof ChallengeConcept) {
                ch = (ChallengeConcept) gc;
                Assert.assertFalse(ch.isCompleted());
            }
        }
    }

    @Test
    public void activeChallengeWithStartAndEndDate() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.setStart(DateTime.now().minusDays(1).toDate());
        challenge.setEnd(DateTime.now().plusDays(1).toDate());
        Date executionMoment = DateTime.now().toDate();
        Assert.assertTrue(challenge.isActive(executionMoment));
    }

    @Test
    public void activeChallengeWithoutStart() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.setEnd(DateTime.now().plusDays(1).toDate());
        Date executionMoment = DateTime.now().toDate();
        Assert.assertTrue(challenge.isActive(executionMoment));
    }

    @Test
    public void inactiveChallengeWithoutStart() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.setEnd(DateTime.now().plusDays(1).toDate());
        Date executionMoment = DateTime.now().plusDays(3).toDate();
        Assert.assertFalse(challenge.isActive(executionMoment));
    }

    @Test
    public void activeChallengeWithoutEnd() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.setStart(DateTime.now().minusDays(1).toDate());
        Date executionMoment = DateTime.now().toDate();
        Assert.assertTrue(challenge.isActive(executionMoment));
    }


    @Test
    public void neverEndingChallenge() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        Date executionMoment = DateTime.parse("1981-02-10T22:33:00").toDate();
        Assert.assertTrue(challenge.isActive(executionMoment));
    }


    @Test
    public void nullExecutionMoment() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.setStart(DateTime.now().minusDays(1).toDate());
        challenge.setEnd(DateTime.now().plusDays(1).toDate());
        Date executionMoment = null;
        Assert.assertTrue(challenge.isActive(executionMoment));
    }

    @Test
    public void invalidExecutionMoment() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.setStart(DateTime.now().minusDays(1).toDate());
        challenge.setEnd(DateTime.now().plusDays(1).toDate());
        long executionMoment = -44;
        Assert.assertTrue(challenge.isActive(executionMoment));
    }


    @Test
    public void unsettedExecutionMoment() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.setStart(DateTime.now().minusDays(1).toDate());
        challenge.setEnd(DateTime.now().plusDays(1).toDate());
        long executionMoment = -1;
        Assert.assertTrue(challenge.isActive(executionMoment));
    }


    @Test
    public void toStringNeverEndindChallenge() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        String expected =
                "{modelName=model1, instance=null, fields={}, start=null, end=null, state=ASSIGNED}";
        Assert.assertEquals(expected, challenge.toString());
    }

    @Test
    public void toStringChallengeWithDates() {
        Date dateOfAssignment = date("2017-04-20T00:12:11");
        BDDMockito.given(clock.now()).willReturn(dateOfAssignment);

        ChallengeConcept challenge = new ChallengeConcept(clock);
        challenge.setModelName("model1");
        challenge.setStart(DateTime.parse("2017-03-11T00:00:00").toDate());
        challenge.setEnd(DateTime.parse("2017-05-01T00:00:00").toDate());
        String expected =
                "{modelName=model1, instance=null, fields={}, start=11/03/2017 00:00:00, end=01/05/2017 00:00:00, state=ASSIGNED}";
        Assert.assertEquals(expected, challenge.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongAssignemnt() {
        Game game = gameSrv.saveGameDefinition(defineGame());

        // add rules
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenges.drl"));
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/itinery.drl"));

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);

        LocalDate today = new LocalDate();
        playerSrv.assignChallenge(GAME, PLAYER, new ChallengeAssignment("prize", null, null, null,
                today.dayOfMonth().addToCopy(-2).toDate(), today.dayOfMonth().addToCopy(-1).toDate()));

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("wrongField", 50);
        playerSrv.assignChallenge(GAME, PLAYER, new ChallengeAssignment("prize", null, params, null,
                today.dayOfMonth().addToCopy(5).toDate(), today.dayOfMonth().addToCopy(7).toDate()));

    }

    @Test
    public void assign_proposed_challenge() {
        gameSrv.saveGameDefinition(defineGame());


        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);

        LocalDate today = new LocalDate();
        LocalDate tomorrow = today.plusDays(1);

        ChallengeConcept challenge =
                playerSrv.assignChallenge(GAME, PLAYER, new ChallengeAssignment("prize", null, null,
                        "PROPOSED", today.toDate(), tomorrow.toDate()));
        assertThat(challenge.getState(), is(ChallengeState.PROPOSED));

    }

    @Test
    public void assign_assigned_challenge() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);

        LocalDate today = new LocalDate();
        LocalDate tomorrow = today.plusDays(1);

        ChallengeConcept challenge =
                playerSrv.assignChallenge(GAME, PLAYER, new ChallengeAssignment("prize", null, null,
                        "assigned", today.toDate(), tomorrow.toDate()));
        assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
    }

    @Test(expected = IllegalArgumentException.class)
    public void assign_non_existent_type() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);

        LocalDate today = new LocalDate();
        LocalDate tomorrow = today.plusDays(1);

        ChallengeConcept challenge =
                playerSrv.assignChallenge(GAME, PLAYER, new ChallengeAssignment("prize", null, null,
                        "DUMMIE", today.toDate(), tomorrow.toDate()));
        assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
    }

    @Test
    public void assign_to_default_value() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);

        LocalDate today = new LocalDate();
        LocalDate tomorrow = today.plusDays(1);

        ChallengeConcept challenge =
                playerSrv.assignChallenge(GAME, PLAYER, new ChallengeAssignment("prize", null, null,
                        null, today.toDate(), tomorrow.toDate()));
        assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
    }

    @Test
    public void force_challenge() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);


        ChallengeAssignment priorited = new ChallengeAssignment();
        priorited.setChallengeType("PROPOSED");
        priorited.setInstanceName("this_value");
        priorited.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", priorited);

        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced.getName(), is("this_value"));
        assertThat(forced.isForced(), is(true));
    }


    @Test
    public void try_to_force_player_without_challenges() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        PlayerState state = playerSrv.loadState(GAME, "player", true, false);
        playerSrv.saveState(state);

        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced, is(nullValue()));
    }

    @Test
    public void try_to_force_player_without_proposed_challenges() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        ChallengeAssignment completed = new ChallengeAssignment();
        completed.setChallengeType("COMPLETED");
        completed.setInstanceName("completed");
        completed.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", completed);

        ChallengeAssignment failed = new ChallengeAssignment();
        failed.setChallengeType("FAILED");
        failed.setInstanceName("failed");
        failed.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", failed);

        ChallengeAssignment firstAssigned = new ChallengeAssignment();
        firstAssigned.setChallengeType("ASSIGNED");
        firstAssigned.setInstanceName("firstAssigned");
        firstAssigned.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", firstAssigned);

        ChallengeAssignment secondAssigned = new ChallengeAssignment();
        secondAssigned.setChallengeType("ASSIGNED");
        secondAssigned.setInstanceName("secondAssigned");
        secondAssigned.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", secondAssigned);

        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced, is(nullValue()));
    }

    @Ignore
    @Test
    /**
     * ATTENTION: comparator doesn't give always the same result..
     */
    public void force_player_proposed_with_same_priority() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        ChallengeAssignment firstProposed = new ChallengeAssignment();
        firstProposed.setChallengeType("PROPOSED");
        firstProposed.setInstanceName("firstProposed");
        firstProposed.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", firstProposed);

        ChallengeAssignment secondProposed = new ChallengeAssignment();
        secondProposed.setChallengeType("PROPOSED");
        secondProposed.setInstanceName("secondProposed");
        secondProposed.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", secondProposed);

        ChallengeAssignment thirdProposed = new ChallengeAssignment();
        thirdProposed.setChallengeType("PROPOSED");
        thirdProposed.setInstanceName("thirdProposed");
        thirdProposed.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", thirdProposed);

        ChallengeAssignment fourthProposed = new ChallengeAssignment();
        fourthProposed.setChallengeType("PROPOSED");
        fourthProposed.setInstanceName("fourthProposed");
        fourthProposed.setModelName("prize");
        playerSrv.assignChallenge(GAME, "player", fourthProposed);


        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced.getName(), is("fourthProposed"));
    }


    @Test
    public void force_player_proposed_with_different_priority() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        ChallengeAssignment firstProposed = new ChallengeAssignment();
        firstProposed.setChallengeType("PROPOSED");
        firstProposed.setInstanceName("firstProposed");
        firstProposed.setModelName("prize");
        firstProposed.setPriority(1);
        playerSrv.assignChallenge(GAME, "player", firstProposed);

        ChallengeAssignment secondProposed = new ChallengeAssignment();
        secondProposed.setChallengeType("PROPOSED");
        secondProposed.setInstanceName("secondProposed");
        secondProposed.setModelName("prize");
        secondProposed.setPriority(5);
        playerSrv.assignChallenge(GAME, "player", secondProposed);

        ChallengeAssignment thirdProposed = new ChallengeAssignment();
        thirdProposed.setChallengeType("PROPOSED");
        thirdProposed.setInstanceName("thirdProposed");
        thirdProposed.setModelName("prize");
        thirdProposed.setPriority(3);
        playerSrv.assignChallenge(GAME, "player", thirdProposed);


        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced.getName(), is("secondProposed"));
    }

    @Test
    public void force_proposed_group_challenge() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        ChallengeAssignment firstProposed = new ChallengeAssignment();
        firstProposed.setChallengeType("PROPOSED");
        firstProposed.setInstanceName("firstProposed");
        firstProposed.setModelName("prize");
        firstProposed.setPriority(1);
        playerSrv.assignChallenge(GAME, "player", firstProposed);

        ChallengeAssignment secondProposed = new ChallengeAssignment();
        secondProposed.setChallengeType("PROPOSED");
        secondProposed.setInstanceName("secondProposed");
        secondProposed.setModelName("prize");
        secondProposed.setPriority(5);
        playerSrv.assignChallenge(GAME, "player", secondProposed);

        GroupChallenge groupChallenge = new GroupChallenge(ChallengeState.PROPOSED);
        groupChallenge.setInstanceName("bestPerformance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        groupChallenge.setGameId(GAME);
        groupChallenge.setPriority(1000);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        Attendee otherPlayer = new Attendee();
        otherPlayer.setPlayerId("otherPlayer");
        otherPlayer.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(otherPlayer);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced.getName(), is("secondProposed"));

    }

    @Test
    public void force_choice_with_2_proposed_group_challenges() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        ChallengeAssignment firstProposed = new ChallengeAssignment();
        firstProposed.setChallengeType("PROPOSED");
        firstProposed.setInstanceName("firstProposed");
        firstProposed.setModelName("prize");
        firstProposed.setPriority(1);
        playerSrv.assignChallenge(GAME, "player", firstProposed);

        ChallengeAssignment secondProposed = new ChallengeAssignment();
        secondProposed.setChallengeType("PROPOSED");
        secondProposed.setInstanceName("secondProposed");
        secondProposed.setModelName("prize");
        secondProposed.setPriority(5);
        playerSrv.assignChallenge(GAME, "player", secondProposed);

        GroupChallenge groupChallenge = new GroupChallenge(ChallengeState.PROPOSED);
        groupChallenge.setInstanceName("bestPerformance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        groupChallenge.setGameId(GAME);
        groupChallenge.setPriority(2);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        Attendee otherPlayer = new Attendee();
        otherPlayer.setPlayerId("otherPlayer");
        otherPlayer.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(otherPlayer);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        GroupChallenge secondGroupChallenge = new GroupChallenge(ChallengeState.PROPOSED);
        secondGroupChallenge.setInstanceName("bestPerformance_1");
        secondGroupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        secondGroupChallenge.setGameId(GAME);
        secondGroupChallenge.setPriority(1);
        player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        secondGroupChallenge.getAttendees().add(player);
        otherPlayer = new Attendee();
        otherPlayer.setPlayerId("otherPlayer");
        otherPlayer.setRole(Role.GUEST);
        secondGroupChallenge.getAttendees().add(otherPlayer);
        secondGroupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(secondGroupChallenge);

        assertThat(
                groupChallengeRepo.playerGroupChallenges(GAME, "player", ChallengeState.ASSIGNED),
                hasSize(0));
        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced.getName(), is("secondProposed"));
        assertThat(
                groupChallengeRepo.playerGroupChallenges(GAME, "player", ChallengeState.ASSIGNED),
                hasSize(0));
        assertThat(
                groupChallengeRepo.playerGroupChallenges(GAME, "player", ChallengeState.PROPOSED),
                hasSize(0));
    }

    @Test
    public void force_choice_with_already_assigned_group_challenge() {
        gameSrv.saveGameDefinition(defineGame());

        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        ChallengeAssignment firstProposed = new ChallengeAssignment();
        firstProposed.setChallengeType("PROPOSED");
        firstProposed.setInstanceName("firstProposed");
        firstProposed.setModelName("prize");
        firstProposed.setPriority(1);
        playerSrv.assignChallenge(GAME, "player", firstProposed);

        ChallengeAssignment secondProposed = new ChallengeAssignment();
        secondProposed.setChallengeType("PROPOSED");
        secondProposed.setInstanceName("secondProposed");
        secondProposed.setModelName("prize");
        secondProposed.setPriority(5);
        playerSrv.assignChallenge(GAME, "player", secondProposed);

        GroupChallenge groupChallenge = new GroupChallenge(ChallengeState.ASSIGNED);
        groupChallenge.setInstanceName("bestPerformance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        groupChallenge.setGameId(GAME);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        Attendee otherPlayer = new Attendee();
        otherPlayer.setPlayerId("otherPlayer");
        otherPlayer.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(otherPlayer);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced.getName(), is("secondProposed"));
    }

    @Test
    public void force_choice_with_an_active_group_challenge() {
        gameSrv.saveGameDefinition(defineGame());


        // define challenge Model
        ChallengeModel modelPrize = new ChallengeModel();
        modelPrize.setName("prize");
        gameSrv.saveChallengeModel(GAME, modelPrize);

        ChallengeAssignment firstProposed = new ChallengeAssignment();
        firstProposed.setChallengeType("PROPOSED");
        firstProposed.setInstanceName("firstProposed");
        firstProposed.setModelName("prize");
        firstProposed.setPriority(1);
        firstProposed.setStart(LocalDateTime.now().plusDays(2).toDate());
        firstProposed.setEnd(LocalDateTime.now().plusDays(5).toDate());
        playerSrv.assignChallenge(GAME, "player", firstProposed);

        ChallengeAssignment secondProposed = new ChallengeAssignment();
        secondProposed.setChallengeType("PROPOSED");
        secondProposed.setInstanceName("secondProposed");
        secondProposed.setModelName("prize");
        secondProposed.setPriority(5);
        secondProposed.setStart(LocalDateTime.now().plusDays(2).toDate());
        secondProposed.setEnd(LocalDateTime.now().plusDays(5).toDate());
        playerSrv.assignChallenge(GAME, "player", secondProposed);

        GroupChallenge groupChallenge = new GroupChallenge(ChallengeState.ASSIGNED);
        groupChallenge.setInstanceName("bestPerformance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        groupChallenge.setGameId(GAME);
        groupChallenge.setPriority(1000);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        Attendee otherPlayer = new Attendee();
        otherPlayer.setPlayerId("otherPlayer");
        otherPlayer.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(otherPlayer);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        groupChallenge.setStart(LocalDateTime.now().minusDays(5).toDate());
        groupChallenge.setEnd(LocalDateTime.now().plusDays(1).toDate());
        challengeSrv.save(groupChallenge);

        ChallengeConcept forced = playerSrv.forceChallengeChoice(GAME, "player");
        assertThat(forced.getName(), is("secondProposed"));
        assertThat(
                groupChallengeRepo.playerGroupChallenges(GAME, "player", ChallengeState.ASSIGNED),
                hasSize(1));
    }

    @Test
    public void should_not_save_group_challenges_in_playerState() {
        Game game = defineGame();
        game = gameSrv.saveGameDefinition(game);
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setGameId(game.getId());
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        PlayerState state = playerSrv.loadState(game.getId(), "player", true, true);
        assertThat(state.challenges(), hasSize(1));
        playerSrv.saveState(state);
        state = playerSrv.loadState(game.getId(), "player", false, true);
        assertThat(state.challenges(), hasSize(1));
    }

    @Test
    public void challenge_failure_one_group_challenge() {
        Game game = defineGame();
        game = gameSrv.saveGameDefinition(game);
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setGameId(game.getId());
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        groupChallenge.setStart(LocalDate.now().minusDays(2).toDate());
        groupChallenge.setEnd(LocalDate.now().minusDays(1).toDate());
        groupChallenge.setState(ChallengeState.ASSIGNED);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        gameSrv.taskChallengeFailure();
        assertThat(groupChallengeRepo.findAll().get(0).getState(), is(ChallengeState.FAILED));
    }

    @Test
    public void challenge_failure_two_group_challenge() {
        Game game = defineGame();
        game = gameSrv.saveGameDefinition(game);
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setGameId(game.getId());
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        groupChallenge.setStart(LocalDate.now().minusDays(2).toDate());
        groupChallenge.setEnd(LocalDate.now().minusDays(1).toDate());
        groupChallenge.setState(ChallengeState.ASSIGNED);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        groupChallenge = new GroupChallenge();
        groupChallenge.setGameId(game.getId());
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_TIME);
        player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        groupChallenge.setStart(LocalDateTime.now().minusHours(10).toDate());
        groupChallenge.setEnd(LocalDateTime.now().minusHours(5).toDate());
        groupChallenge.setState(ChallengeState.ASSIGNED);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        gameSrv.taskChallengeFailure();
        List<GroupChallenge> groupChallenges = groupChallengeRepo.findAll();
        long failedCounter = groupChallenges.stream()
                .filter(challenge -> challenge.getState() == ChallengeState.FAILED).count();
        assertThat(groupChallenges, hasSize(2));
        assertThat(failedCounter, is(2L));
    }

    @Test
    public void challenge_failure_three_challenges_two_failure() {
        Game game = defineGame();
        game = gameSrv.saveGameDefinition(game);
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setGameId(game.getId());
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        Attendee player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        groupChallenge.setStart(LocalDate.now().minusDays(2).toDate());
        groupChallenge.setEnd(LocalDate.now().minusDays(1).toDate());
        groupChallenge.setState(ChallengeState.ASSIGNED);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        groupChallenge = new GroupChallenge();
        groupChallenge.setGameId(game.getId());
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_TIME);
        player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        groupChallenge.setStart(LocalDateTime.now().minusHours(10).toDate());
        groupChallenge.setEnd(LocalDateTime.now().minusHours(5).toDate());
        groupChallenge.setState(ChallengeState.ASSIGNED);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        groupChallenge = new GroupChallenge();
        groupChallenge.setGameId(game.getId());
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COOPERATIVE);
        player = new Attendee();
        player.setPlayerId("player");
        player.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(player);
        groupChallenge.setStart(LocalDateTime.now().minusDays(1).toDate());
        groupChallenge.setEnd(LocalDateTime.now().plusDays(5).toDate());
        groupChallenge.setState(ChallengeState.ASSIGNED);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", null));
        challengeSrv.save(groupChallenge);

        gameSrv.taskChallengeFailure();
        List<GroupChallenge> groupChallenges = groupChallengeRepo.findAll();
        long failedCounter = groupChallenges.stream()
                .filter(challenge -> challenge.getState() == ChallengeState.FAILED).count();
        assertThat(groupChallenges, hasSize(3));
        assertThat(failedCounter, is(2L));
    }


    @Test
    public void update_score_in_competitive_time() {
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_TIME);
        Attendee player = new Attendee();
        player.setPlayerId("proposer");
        player.setRole(Role.PROPOSER);
        groupChallenge.getAttendees().add(player);
        Attendee guest = new Attendee();
        guest.setPlayerId("guest");
        guest.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(guest);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", "weekly"));
        groupChallenge.setChallengeTarget(22d);
        
        PointConcept proposerGreenLeaves = new PointConcept("green leaves");
        proposerGreenLeaves.addPeriod("weekly", LocalDate.now().minusDays(2).toDate(), 604800000);
        proposerGreenLeaves.setScore(5d);
        PlayerState proposer = new PlayerState();
        proposer.setPlayerId("proposer");
        proposer.getState().add(proposerGreenLeaves);

        groupChallenge = groupChallenge.update(proposer, System.currentTimeMillis());
        Attendee prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(5d));
        
        proposerGreenLeaves.setScore(25d);
        groupChallenge = groupChallenge.update(proposer, System.currentTimeMillis());
        prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(22d));
    }

    @Test
    public void update_score_in_competitive_time_2_players_scenario() {
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_TIME);
        Attendee player = new Attendee();
        player.setPlayerId("proposer");
        player.setRole(Role.PROPOSER);
        groupChallenge.getAttendees().add(player);
        Attendee guest = new Attendee();
        guest.setPlayerId("guest");
        guest.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(guest);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", "weekly"));
        groupChallenge.setChallengeTarget(22d);

        PointConcept proposerGreenLeaves = new PointConcept("green leaves");
        proposerGreenLeaves.addPeriod("weekly", LocalDate.now().minusDays(2).toDate(), 604800000);
        proposerGreenLeaves.setScore(5d);
        PlayerState proposerState = new PlayerState();
        proposerState.setPlayerId("proposer");
        proposerState.getState().add(proposerGreenLeaves);

        groupChallenge = groupChallenge.update(proposerState, System.currentTimeMillis());
        Attendee prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(5d));



        PointConcept guestGreenLeaves = new PointConcept("green leaves");
        guestGreenLeaves.addPeriod("weekly", LocalDate.now().minusDays(2).toDate(), 604800000);
        guestGreenLeaves.setScore(10d);
        PlayerState guestState = new PlayerState();
        guestState.setPlayerId("guest");
        guestState.getState().add(guestGreenLeaves);

        groupChallenge = groupChallenge.update(guestState, System.currentTimeMillis());
        Attendee g = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("guest")).findFirst().get();
        assertThat(g.getChallengeScore(), is(10d));

        proposerGreenLeaves.setScore(25d);
        groupChallenge = groupChallenge.update(proposerState, System.currentTimeMillis());
        prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(22d));
    }


    @Test
    public void update_score_in_cooperative() {
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COOPERATIVE);
        Attendee player = new Attendee();
        player.setPlayerId("proposer");
        player.setRole(Role.PROPOSER);
        groupChallenge.getAttendees().add(player);
        Attendee guest = new Attendee();
        guest.setPlayerId("guest");
        guest.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(guest);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", "weekly"));
        groupChallenge.setChallengeTarget(22d);

        PointConcept proposerGreenLeaves = new PointConcept("green leaves");
        proposerGreenLeaves.addPeriod("weekly", LocalDate.now().minusDays(2).toDate(), 604800000);
        proposerGreenLeaves.setScore(22d);
        PlayerState proposerState = new PlayerState();
        proposerState.setPlayerId("proposer");
        proposerState.getState().add(proposerGreenLeaves);

        groupChallenge = groupChallenge.update(proposerState, System.currentTimeMillis());
        Attendee prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(22d));
    }

    @Test
    public void update_score_in_cooperative_with_score_higher_of_challenge_score() {
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COOPERATIVE);
        Attendee player = new Attendee();
        player.setPlayerId("proposer");
        player.setRole(Role.PROPOSER);
        groupChallenge.getAttendees().add(player);
        Attendee guest = new Attendee();
        guest.setPlayerId("guest");
        guest.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(guest);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", "weekly"));
        groupChallenge.setChallengeTarget(22d);

        PointConcept proposerGreenLeaves = new PointConcept("green leaves");
        proposerGreenLeaves.addPeriod("weekly", LocalDate.now().minusDays(2).toDate(), 604800000);
        proposerGreenLeaves.setScore(25d);
        PlayerState proposerState = new PlayerState();
        proposerState.setPlayerId("proposer");
        proposerState.getState().add(proposerGreenLeaves);

        groupChallenge = groupChallenge.update(proposerState, System.currentTimeMillis());
        Attendee prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(22d));
    }


    @Test
    public void update_score_in_cooperative_2_players_scenario() {
        GroupChallenge groupChallenge = new GroupChallenge();
        groupChallenge.setInstanceName("groupChallengeInstance");
        groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COOPERATIVE);
        Attendee player = new Attendee();
        player.setPlayerId("proposer");
        player.setRole(Role.PROPOSER);
        groupChallenge.getAttendees().add(player);
        Attendee guest = new Attendee();
        guest.setPlayerId("guest");
        guest.setRole(Role.GUEST);
        groupChallenge.getAttendees().add(guest);
        groupChallenge.setChallengePointConcept(new PointConceptRef("green leaves", "weekly"));
        groupChallenge.setChallengeTarget(22d);

        PointConcept proposerGreenLeaves = new PointConcept("green leaves");
        proposerGreenLeaves.addPeriod("weekly", LocalDate.now().minusDays(2).toDate(), 604800000);
        proposerGreenLeaves.setScore(5d);
        PlayerState proposerState = new PlayerState();
        proposerState.setPlayerId("proposer");
        proposerState.getState().add(proposerGreenLeaves);

        groupChallenge = groupChallenge.update(proposerState, System.currentTimeMillis());
        Attendee prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(5d));



        PointConcept guestGreenLeaves = new PointConcept("green leaves");
        guestGreenLeaves.addPeriod("weekly", LocalDate.now().minusDays(2).toDate(), 604800000);
        guestGreenLeaves.setScore(10d);
        PlayerState guestState = new PlayerState();
        guestState.setPlayerId("guest");
        guestState.getState().add(guestGreenLeaves);

        groupChallenge = groupChallenge.update(guestState, System.currentTimeMillis());
        Attendee g = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("guest")).findFirst().get();
        assertThat(g.getChallengeScore(), is(10d));

        proposerGreenLeaves.setScore(25d);
        groupChallenge = groupChallenge.update(proposerState, System.currentTimeMillis());
        prop = groupChallenge.getAttendees().stream()
                .filter(a -> a.getPlayerId().equals("proposer")).findFirst().get();
        assertThat(prop.getChallengeScore(), is(12d));
    }


    @Test
    public void assign_a_hidden_challenge_in_game_without_settings() {
        final String GAME = "noSettingsGame";
        Game noSettingsGame = new Game(GAME);
        gameSrv.saveGameDefinition(noSettingsGame);

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);


        ChallengeAssignment assignment = new ChallengeAssignment("prize", null, null,
                "assigned", null, null);
        assignment.setHide(true);
        
        ChallengeConcept challenge =
                playerSrv.assignChallenge(GAME, PLAYER, assignment);
        
        assertThat(challenge.getVisibility().isHidden(), is(true));
    }

    /*
     * Test actually commented because assignChallenge is bound to System actual time to calculate
     * the next disclosure, so test is not repeatable
     */
    // @Test
    public void assign_a_hidden_challenge_in_game_with_challenge_settings() {
        final String GAME = "noSettingsGame";
        Game noSettingsGame = new Game(GAME);

        noSettingsGame.getSettings().getChallengeSettings().getDisclosure()
                .setStartDate(date("2019-10-10T20:00:00"));
        noSettingsGame.getSettings().getChallengeSettings().getDisclosure()
                .setFrequency(new TimeInterval(1, TimeUnit.DAY));
        gameSrv.saveGameDefinition(noSettingsGame);

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);


        ChallengeAssignment assignment =
                new ChallengeAssignment("prize", null, null, "proposed", null, null);
        assignment.setHide(true);

        ChallengeConcept challenge = playerSrv.assignChallenge(GAME, PLAYER, assignment);

        // NOTE: I modified clock of challenge to simulate behavior after disclosure
        challenge.setClock(new ExecutionClock(date("2019-10-25T10:00:00").getTime()));
        assertThat(challenge.isHidden(), is(false));
        assertThat(challenge.getVisibility().getDisclosureDate(), is(date("2019-10-22T20:00:00")));
    }

    @Test
    public void assign_a_public_challenge_in_game_with_challenge_settings() {
        final String GAME = "noSettingsGame";
        Game noSettingsGame = new Game(GAME);

        noSettingsGame.getSettings().getChallengeSettings().getDisclosure()
                .setStartDate(date("2019-10-10T20:00:00"));
        noSettingsGame.getSettings().getChallengeSettings().getDisclosure()
                .setFrequency(new TimeInterval(1, TimeUnit.DAY));
        gameSrv.saveGameDefinition(noSettingsGame);

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);


        ChallengeAssignment assignment =
                new ChallengeAssignment("prize", null, null, "proposed", null, null);
        assignment.setHide(false);

        ChallengeConcept challenge = playerSrv.assignChallenge(GAME, PLAYER, assignment);

        assertThat(challenge.isHidden(), is(false));
        assertThat(challenge.getVisibility().getDisclosureDate(), is(nullValue()));
    }


    private Game defineGame() {

        final String OWNER = "gameMaster";

        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);
        game.setOwner(OWNER);
        game.setDomain(DOMAIN);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);

        game.setConcepts(new HashSet<GameConcept>());
        game.getConcepts().add(new PointConcept("green leaves"));

        game.setTasks(new HashSet<GameTask>());
        return game;
    }

    private Date date(String isoDate) {
        return LocalDateTime.parse(isoDate).toDate();
    }
}
