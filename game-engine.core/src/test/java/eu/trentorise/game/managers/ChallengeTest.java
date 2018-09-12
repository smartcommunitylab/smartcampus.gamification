package eu.trentorise.game.managers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
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
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class ChallengeTest {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

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
        mongo.getDb().dropDatabase();
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
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));

        // define challenge Model
        ChallengeModel model1 = new ChallengeModel();
        model1.setName("prize");
        gameSrv.saveChallengeModel(GAME, model1);



        LocalDate today = new LocalDate();
        ChallengeAssignment assignment = new ChallengeAssignment("prize", null, null, null,
                today.toDate(), today.dayOfMonth().addToCopy(2).toDate());
        playerSrv.assignChallenge(GAME, PLAYER, assignment);

        PlayerState p = playerSrv.loadState(GAME, PLAYER, false);

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
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));

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

        PlayerState p = playerSrv.loadState(GAME, PLAYER, false);

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
        gameSrv.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));

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
