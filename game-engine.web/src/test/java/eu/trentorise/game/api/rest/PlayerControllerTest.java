package eu.trentorise.game.api.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.bean.ChallengeAssignmentDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.NoSecurityConfig;
import eu.trentorise.game.managers.ChallengeManager;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.Inventory.ItemChoice;
import eu.trentorise.game.model.Inventory.ItemChoice.ChoiceType;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Config;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.LevelInstance;
import eu.trentorise.game.model.PlayerBlackList;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ArchivedConcept;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("no-sec")
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, NoSecurityConfig.class,
        TestMVCConfiguration.class},
        loader = AnnotationConfigWebContextLoader.class)
@TestPropertySource(properties = {"game.createDemo=false"})
@WebAppConfiguration
public class PlayerControllerTest {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private ChallengeManager challengeSrv;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mocker;

    private ObjectMapper mapper;

    private static final String GAME = "gameTest";
    private static final String ACTION = "save_itinerary";

    @PostConstruct
    public void init() {
        mocker = MockMvcBuilders.webAppContextSetup(wac).build();
        mapper = new ObjectMapper();
    }

    @Before
    public void cleanDB() {
        // clean mongo
        mongo.getDb().drop();
    }

    private Game defineGame() {
        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);
        game.getActions().add("classification");

        PointConcept walkKm = new PointConcept("Walk_Km");
        walkKm.addPeriod("weekly", new Date(), 60000);
        game.setConcepts(new HashSet<GameConcept>());
        game.getConcepts().add(walkKm);

        game.setTasks(new HashSet<GameTask>());

        return game;
    }

    @Test
    public void createPlayer() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        try {
            PlayerStateDTO player = new PlayerStateDTO();
            player.setPlayerId("10001");
            RequestBuilder builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(player));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void propose_a_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setState("proposed");


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));
            
            PlayerState player = playerSrv.loadState(game.getId(), "10001", false, false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.challenges().stream().findFirst().orElse(null);
            assertThat(challenge.getState(), is(ChallengeState.PROPOSED));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }

    }
    
    
    @Test
    public void assign_a_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));
            
            PlayerState player = playerSrv.loadState(game.getId(), "10001", false, false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.challenges().stream().findFirst().orElse(null);
            assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void propose_a_challenge_with_priority_5() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setState("proposed");
        assignment.setPriority(5);


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState player = playerSrv.loadState(game.getId(), "10001", false, false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.challenges().stream().findFirst().orElse(null);
            assertThat(challenge.getState(), is(ChallengeState.PROPOSED));
            assertThat(challenge.getPriority(), is(5));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }

    }

    @Test
    public void assign_invalid_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setState("dummie");


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }

    }

    @Test
    public void assign_challenge_without_declare_type_of_assignment() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");

        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState player = playerSrv.loadState(game.getId(), "10001", false, false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.challenges().stream().findFirst().orElse(null);
            assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void set_origin_for_challenge_assignment() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setOrigin("MY_SYSTEM");

        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState player = playerSrv.loadState(game.getId(), "10001", false, false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.challenges().stream().findFirst().orElse(null);
            assertThat(challenge.getOrigin(), is("MY_SYSTEM"));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    // test reading player state data


    @Test
    public void read_inventory() {
        Game game = defineGame();

        Level levelDefinition = new Level("greener","green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        Config config = new Config();
        config.setChoices(1);
        config.getAvailableModels().addAll(Arrays.asList("model1", "model2"));
        beginner.setConfig(config);
        levelDefinition.getThresholds().add(beginner);
        
        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);
        
        PlayerState player = playerSrv.loadState(game.getId(), "10001", true, false);
        player.getLevels().add(new PlayerLevel(levelDefinition, 10d));
        player.updateInventory(game, Arrays.asList(new LevelInstance("greener", "beginner")));
        playerSrv.saveState(player);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/inventory", game.getId(), "10001");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeChoices", hasSize(2)))
                    .andExpect(jsonPath("$.challengeChoices[0].modelName", is("model1")))
                    .andExpect(jsonPath("$.challengeChoices[0].state", is("AVAILABLE")));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void read_empty_inventory() {
        Game game = defineGame();

        Level levelDefinition = new Level("greener", "green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        levelDefinition.getThresholds().add(beginner);

        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);

        PlayerState player = playerSrv.loadState(game.getId(), "10001", true, false);
        player.getLevels().add(new PlayerLevel(levelDefinition, 10d));
        player.updateInventory(game, null);
        playerSrv.saveState(player);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/player/{playerId}/inventory",
                    game.getId(), "10001");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeChoices", hasSize(0)));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }


    @Test
    public void activate_a_choice() {
        Game game = defineGame();
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        Config config = new Config();
        config.setChoices(1);
        config.getAvailableModels().add("model1");
        beginner.setConfig(config);
        levelDefinition.getThresholds().add(beginner);

        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);



        RequestBuilder builder = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ItemChoice choice = new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1");

            PlayerState player = playerSrv.loadState(game.getId(), "10001", true, false);

            player.updateInventory(game, Arrays.asList(new LevelInstance("greener", "beginner")));
            playerSrv.saveState(player);

            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/inventory/activate", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(choice));

            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeActivationActions", is(0)))
                    .andExpect(jsonPath("$.challengeChoices[0].state", is("ACTIVE")));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void no_more_activation_actions() {
        Game game = defineGame();
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        Config config = new Config();
        config.setChoices(1);
        config.getAvailableModels().add("model1");
        config.getAvailableModels().add("model2");
        beginner.setConfig(config);
        levelDefinition.getThresholds().add(beginner);

        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);

        RequestBuilder builder = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ItemChoice choice = new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1");

            PlayerState player = playerSrv.loadState(game.getId(), "10001", true, false);

            player.updateInventory(game, Arrays.asList(new LevelInstance("greener", "beginner")));
            playerSrv.saveState(player);

            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/inventory/activate", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(choice));

            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeActivationActions", is(0)))
                    .andExpect(jsonPath("$.challengeChoices[0].state", is("ACTIVE")));

            ItemChoice choice1 = new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1");
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/inventory/activate", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(choice1));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void accept_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);
        


        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);

            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                            game.getId(),
                            "player", "instance_name");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));
            PlayerState loaded = playerSrv.loadState(game.getId(), "player", false, false);

            ChallengeConcept challenge = loaded.challenges().stream()
                    .filter(ch -> ch.getName().equals("instance_name")).findFirst()
                    .orElse(null);
            assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));

        } catch (Exception e) {

        }

    }

    @Test
    public void try_to_accept_a_completed_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);



        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "COMPLETED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "instance_name");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {

        }

    }

    @Test
    public void try_to_accept_a_not_existent_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "notExistentInstanceName");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {

        }
    }

    @Test
    public void accept_challenge_and_clean_others_proposed() {

        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);
            ChallengeAssignment assignment2 = new ChallengeAssignment("model_1", "instance_name_1",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment2);
            ChallengeAssignment assignment3 = new ChallengeAssignment("model_1", "instance_name_2",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment3);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "instance_name_1");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState loaded = playerSrv.loadState(game.getId(), "player", false, false);
            List<ChallengeConcept> proposed = loaded.challenges().stream()
                    .filter(ch -> ch.getState() == ChallengeState.PROPOSED)
                    .collect(Collectors.toList());

            assertThat(proposed, hasSize(0));

        } catch (Exception e) {

        }
    }



    @Test
    public void accept_challenge_and_clean_only_proposed() {

        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);
        ChallengeModel model2 = new ChallengeModel();
        model2.setGameId(game.getId());
        model2.setName("model_2");
        gameSrv.saveChallengeModel(game.getId(), model2);

        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);
            ChallengeAssignment assignment2 = new ChallengeAssignment("model_1", "instance_name_1",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment2);
            ChallengeAssignment assignment3 = new ChallengeAssignment("model_1", "instance_name_2",
                    new HashMap<>(), "COMPLETED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment3);
            ChallengeAssignment assignment4 = new ChallengeAssignment("model_2", "instance_name_3",
                    new HashMap<>(), "ACTIVE", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment4);
            ChallengeAssignment assignment5 = new ChallengeAssignment("model_2", "instance_name_4",
                    new HashMap<>(), "FAILED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment5);
            
            GroupChallenge groupChallenge = new GroupChallenge();
            groupChallenge.setGameId(game.getId());
            groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
            groupChallenge.setState(ChallengeState.PROPOSED);
            Attendee proposer = new Attendee();
            proposer.setPlayerId("player1");
            proposer.setRole(Role.PROPOSER);
            groupChallenge.getAttendees().add(proposer);
            Attendee guest = new Attendee();
            guest.setPlayerId("player");
            guest.setRole(Role.GUEST);
            groupChallenge.getAttendees().add(guest);
            challengeSrv.save(groupChallenge);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "instance_name_1");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState loaded = playerSrv.loadState(game.getId(), "player", false, true);
            List<ChallengeConcept> proposed = loaded.challenges().stream()
                    .filter(ch -> ch.getState() == ChallengeState.PROPOSED)
                    .collect(Collectors.toList());

            assertThat(proposed, hasSize(0));
            assertThat(loaded.getState(), hasSize(4));
        } catch (Exception e) {

        }
    }

    @Test
    public void read_player_state_with_only_a_group_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);
        GroupChallenge challenge = new GroupChallenge();
        challenge.setGameId(game.getId());
        challenge.setInstanceName("instanceName");
        challenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        challenge.setStart(new LocalDateTime().minusDays(1).toDate());
        challenge.setEnd(new LocalDateTime().plusDays(1).toDate());
        challenge.setOrigin("Demiurgo");
        challenge.setPriority(11);
        Attendee proposer = new Attendee();
        proposer.setPlayerId("player");
        proposer.setRole(Role.PROPOSER);
        proposer.setChallengeScore(4.2);
        Attendee guest = new Attendee();
        guest.setRole(Role.GUEST);
        guest.setPlayerId("player2");
        guest.setChallengeScore(1.0);
        challenge.getAttendees().add(proposer);
        challenge.getAttendees().add(guest);

        challenge.setChallengePointConcept(new PointConceptRef("Walk_Km", "weekly"));
        challengeSrv.save(challenge);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders.get(
                    "/data/game/{gameId}/player/{playerId}/state",
                    game.getId(), "player");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.state.ChallengeConcept", hasSize(1)))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].name", is("instanceName")))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].modelName",
                            is("groupCompetitivePerformance")))
                    .andExpect(
                            jsonPath("$.state.ChallengeConcept[0].fields.challengeScore", is(4.2)))
                    .andExpect(
                            jsonPath("$.state.ChallengeConcept[0].fields.otherAttendeeScores",
                                    hasSize(1)));

        } catch (Exception e) {

        }
    }

    @Test
    public void read_player_state_with_a_single_challenge_and_a_group_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);
        
        ChallengeConcept singlePlayerChallenge = new ChallengeConcept();
        singlePlayerChallenge.setModelName("absoluteIncrement");
        singlePlayerChallenge.setName("incrementInstance");
        singlePlayerChallenge.getFields().put("difficulty", 2.0);
        singlePlayerChallenge.getFields().put("wi", 10);
        singlePlayerChallenge.getFields().put("bonusScore", 200);
        player.getState().add(singlePlayerChallenge);

        playerSrv.saveState(player);
        GroupChallenge challenge = new GroupChallenge();
        challenge.setGameId(game.getId());
        challenge.setInstanceName("instanceName");
        challenge.setStart(new LocalDateTime().minusDays(1).toDate());
        challenge.setEnd(new LocalDateTime().plusDays(1).toDate());
        challenge.setOrigin("Demiurgo");
        challenge.setPriority(11);
        Attendee proposer = new Attendee();
        proposer.setPlayerId("player");
        proposer.setRole(Role.PROPOSER);
        proposer.setChallengeScore(4.2);
        Attendee guest = new Attendee();
        guest.setRole(Role.GUEST);
        guest.setPlayerId("player2");
        guest.setChallengeScore(1.0);
        challenge.getAttendees().add(proposer);
        challenge.getAttendees().add(guest);

        challenge.setChallengePointConcept(new PointConceptRef("Walk_Km", "weekly"));
        challenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        challengeSrv.save(challenge);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/player/{playerId}/state",
                    game.getId(), "player");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.state.ChallengeConcept", hasSize(2)));
        } catch (Exception e) {

        }
    }

    @Test
    public void read_player_state_with_group_challenge_with_multiple_participants() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);

        GroupChallenge challenge = new GroupChallenge();
        challenge.setGameId(game.getId());
        challenge.setInstanceName("instanceName");
        challenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        challenge.setStart(new LocalDateTime().minusDays(1).toDate());
        challenge.setEnd(new LocalDateTime().plusDays(1).toDate());
        challenge.setOrigin("Demiurgo");
        challenge.setPriority(11);
        Attendee proposer = new Attendee();
        proposer.setPlayerId("player");
        proposer.setRole(Role.PROPOSER);
        proposer.setChallengeScore(4.2);
        Attendee guest1 = new Attendee();
        guest1.setRole(Role.GUEST);
        guest1.setPlayerId("player2");
        guest1.setChallengeScore(1.0);
        Attendee guest2 = new Attendee();
        guest2.setRole(Role.GUEST);
        guest2.setPlayerId("player3");
        guest2.setChallengeScore(2.0);

        challenge.getAttendees().add(proposer);
        challenge.getAttendees().add(guest1);
        challenge.getAttendees().add(guest2);

        challenge.setChallengePointConcept(new PointConceptRef("Walk_Km", "weekly"));
        challengeSrv.save(challenge);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/player/{playerId}/state",
                    game.getId(), "player");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.state.ChallengeConcept", hasSize(1)))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].name", is("instanceName")))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].modelName",
                            is("groupCompetitivePerformance")))
                    .andExpect(
                            jsonPath("$.state.ChallengeConcept[0].fields.challengeScore", is(4.2)))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].fields.otherAttendeeScores",
                            hasSize(2)));
        } catch (Exception e) {

        }
    }

    @Test
    public void read_player_state_with_a_proposed_group_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        PlayerState player = playerSrv.loadState(game.getId(), "player", true, false);

        GroupChallenge challenge = new GroupChallenge(ChallengeState.PROPOSED);
        challenge.setGameId(game.getId());
        challenge.setInstanceName("instanceName");
        challenge.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        challenge.setStart(new LocalDateTime().minusDays(1).toDate());
        challenge.setEnd(new LocalDateTime().plusDays(1).toDate());
        challenge.setOrigin("rs");
        Attendee proposer = new Attendee();
        proposer.setPlayerId("player");
        proposer.setRole(Role.PROPOSER);
        Attendee guest1 = new Attendee();
        guest1.setRole(Role.GUEST);
        guest1.setPlayerId("player2");

        challenge.getAttendees().add(proposer);
        challenge.getAttendees().add(guest1);

        challenge.setChallengePointConcept(new PointConceptRef("Walk_Km", "weekly"));
        challengeSrv.save(challenge);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/player/{playerId}/state",
                    game.getId(), "player");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.state.ChallengeConcept", hasSize(1)))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].name", is("instanceName")))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].state", is("PROPOSED")))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].modelName",
                            is("groupCompetitivePerformance")))
                    .andExpect(
                            jsonPath("$.state.ChallengeConcept[0].fields.challengeScore", is(0.0)))
                    .andExpect(jsonPath("$.state.ChallengeConcept[0].fields.otherAttendeeScores",
                            hasSize(1)));
        } catch (Exception e) {

        }
    }

    @Test
    public void add_player_to_blacklist() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/block/{otherPlayerId}", game.getId(),
                    "ant-man", "wasp").contentType(MediaType.APPLICATION_JSON);
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.blockedPlayers", hasSize(1)));

        } catch (Exception e) {
            fail("exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void remove_player_from_blacklist_when_not_already_existent() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/unblock/{otherPlayerId}",
                            game.getId(), "ant-man", "wasp")
                    .contentType(MediaType.APPLICATION_JSON);
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.blockedPlayers", hasSize(0)));

        } catch (Exception e) {
            fail("exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void remove_player_from_blacklist() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        PlayerBlackList blacklist = new PlayerBlackList();
        blacklist.setPlayerId("ant-man");
        blacklist.setGameId(game.getId());
        blacklist.getBlockedPlayers().add("wasp");
        blacklist.getBlockedPlayers().add("dr. strange");
        mongo.save(blacklist);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/unblock/{otherPlayerId}",
                            game.getId(), "ant-man", "wasp")
                    .contentType(MediaType.APPLICATION_JSON);
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.blockedPlayers", hasSize(1)));

        } catch (Exception e) {
            fail("exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void remove_player_from_blacklist_not_present_player() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        PlayerBlackList blacklist = new PlayerBlackList();
        blacklist.setPlayerId("ant-man");
        blacklist.setGameId(game.getId());
        blacklist.getBlockedPlayers().add("wasp");
        blacklist.getBlockedPlayers().add("dr. strange");
        mongo.save(blacklist);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/unblock/{otherPlayerId}",
                            game.getId(), "ant-man", "eddie brock")
                    .contentType(MediaType.APPLICATION_JSON);
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.blockedPlayers", hasSize(2)));

        } catch (Exception e) {
            fail("exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void remove_player_from_blacklist_when_it_is_empty() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        PlayerBlackList blacklist = new PlayerBlackList();
        blacklist.setPlayerId("ant-man");
        blacklist.setGameId(game.getId());
        mongo.save(blacklist);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/unblock/{otherPlayerId}",
                            game.getId(), "ant-man", "wasp")
                    .contentType(MediaType.APPLICATION_JSON);
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.blockedPlayers", hasSize(0)));

        } catch (Exception e) {
            fail("exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_player_blacklist_when_not_already_existent() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/blacklist", game.getId(), "ant-man")
                    .contentType(MediaType.APPLICATION_JSON);
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.blockedPlayers", hasSize(0)));

        } catch (Exception e) {
            fail("exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_player_blacklist() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        PlayerBlackList blacklist = new PlayerBlackList();
        blacklist.setPlayerId("ant-man");
        blacklist.setGameId(game.getId());
        blacklist.getBlockedPlayers().add("dr .strange");
        blacklist.getBlockedPlayers().add("wasp");
        mongo.save(blacklist);

        RequestBuilder builder = null;
        try {
            builder =
                    MockMvcRequestBuilders.get("/data/game/{gameId}/player/{playerId}/blacklist",
                            game.getId(), "ant-man").contentType(MediaType.APPLICATION_JSON);
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.blockedPlayers", hasSize(2)));

        } catch (Exception e) {
            fail("exception thrown: " + e.getMessage());
        }
    }

    public void read_empty_archive() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get(
                    "/data/game/{gameId}/archive",
                    game.getId());
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(0)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_given_a_state() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("state", "AUTO_DISCARDED");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(0)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_given_a_player() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_2");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_from_a_date() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-06T05:00:00"));
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T10:00:00"));
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1")
                    .param("from", "" + date("2018-11-07T02:00:00").getTime());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }


    @Test
    public void read_archive_to_a_date() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-06T05:00:00"));
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T10:00:00"));
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1")
                    .param("to", "" + date("2018-11-07T02:00:00").getTime());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_between_a_date() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-06T05:00:00"));
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T10:00:00"));

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T11:00:00"));
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1")
                    .param("from", "" + date("2018-11-07T01:00:00").getTime())
                    .param("to", "" + date("2018-11-07T12:00:00").getTime());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

	@Test
	public void read_system_playersState() {
		final String gameId = "PST_GAME";
		Game g = new Game(gameId);
		g.setConcepts(new HashSet<>());
		g.getConcepts().add(new PointConcept("green"));
		g = gameSrv.saveGameDefinition(g);

		Level level = new Level("Eco Warrior", "green");
		level.getThresholds().add(new Threshold("newbie", 0));
		level.getThresholds().add(new Threshold("adept", 100));
		level.getThresholds().add(new Threshold("master", 1000));
		gameSrv.upsertLevel(gameId, level);

		PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
		p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
		playerSrv.saveState(p);

		PlayerState available = new PlayerState(gameId, "av1");
		available.updateLevels(Arrays.asList(new PlayerLevel(level, 400d)));
		playerSrv.saveState(available);

		PlayerState available2 = new PlayerState(gameId, "av2");
		available2.updateLevels(Arrays.asList(new PlayerLevel(level, 500d)));
		playerSrv.saveState(available2);

		RequestBuilder builder = null;
		try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/challengers", gameId, "proposer")
					.contentType(MediaType.APPLICATION_JSON);
			mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
					.andExpect(jsonPath("$", hasSize(2)));

		} catch (Exception e) {
			fail("exception thrown: " + e.getMessage());
		}

	}

	@Test
	public void read_system_playersState_with_blacklist() {
		final String gameId = "PST_GAME";
		Game g = new Game(gameId);
		g.setConcepts(new HashSet<>());
		g.getConcepts().add(new PointConcept("green"));
		g = gameSrv.saveGameDefinition(g);

		Level level = new Level("Eco Warrior", "green");
		level.getThresholds().add(new Threshold("newbie", 0));
		level.getThresholds().add(new Threshold("adept", 100));
		level.getThresholds().add(new Threshold("master", 1000));
		gameSrv.upsertLevel(gameId, level);

		PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
		p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
		playerSrv.saveState(p);

		PlayerState available = new PlayerState(gameId, "av1");
		available.updateLevels(Arrays.asList(new PlayerLevel(level, 400d)));
		playerSrv.saveState(available);

		PlayerState unavailable = new PlayerState(gameId, "unAvailable");
		unavailable.updateLevels(Arrays.asList(new PlayerLevel(level, 500d)));
		playerSrv.saveState(unavailable);

		PlayerBlackList blacklist = new PlayerBlackList();
		blacklist.setPlayerId("proposer");
		blacklist.setGameId(gameId);
		blacklist.getBlockedPlayers().add("unAvailable");
		mongo.save(blacklist);

		RequestBuilder builder = null;
		try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/challengers", gameId, "proposer")
					.contentType(MediaType.APPLICATION_JSON);
			mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
					.andExpect(jsonPath("$", hasSize(1)));

		} catch (Exception e) {
			fail("exception thrown: " + e.getMessage());
		}

	}
	
	@Test
	public void read_system_playersState_with_propeser_inside_blacklist_of_blockers() {
		final String gameId = "PST_GAME";
		Game g = new Game(gameId);
		g.setConcepts(new HashSet<>());
		g.getConcepts().add(new PointConcept("green"));
		g = gameSrv.saveGameDefinition(g);

		Level level = new Level("Eco Warrior", "green");
		level.getThresholds().add(new Threshold("newbie", 0));
		level.getThresholds().add(new Threshold("adept", 100));
		level.getThresholds().add(new Threshold("master", 1000));
		gameSrv.upsertLevel(gameId, level);

		PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
		p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
		playerSrv.saveState(p);

		PlayerState available = new PlayerState(gameId, "av1");
		available.updateLevels(Arrays.asList(new PlayerLevel(level, 400d)));
		playerSrv.saveState(available);

		PlayerBlackList blacklist = new PlayerBlackList();
		blacklist.setPlayerId("av1");
		blacklist.setGameId(gameId);
		blacklist.getBlockedPlayers().add("proposer");
		mongo.save(blacklist);

		RequestBuilder builder = null;
		try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/challengers", gameId, "proposer")
					.contentType(MediaType.APPLICATION_JSON);
			mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
					.andExpect(jsonPath("$", hasSize(0)));

		} catch (Exception e) {
			fail("exception thrown: " + e.getMessage());
		}

	}

	@Test
	public void read_system_playersState_range_test() {
		final String gameId = "PST_GAME";
		Game g = new Game(gameId);
		g.setConcepts(new HashSet<>());
		g.getConcepts().add(new PointConcept("green"));
		g = gameSrv.saveGameDefinition(g);

		Level level = new Level("Eco Warrior", "green");
		level.getThresholds().add(new Threshold("newbie", 0));
		level.getThresholds().add(new Threshold("adept", 100));
		level.getThresholds().add(new Threshold("master", 1000));
		gameSrv.upsertLevel(gameId, level);

		PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
		p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
		playerSrv.saveState(p);

		PlayerState available = new PlayerState(gameId, "av1");
		available.updateLevels(Arrays.asList(new PlayerLevel(level, 400d)));
		playerSrv.saveState(available);

		PlayerState unavailable = new PlayerState(gameId, "unAvailable");
		unavailable.updateLevels(Arrays.asList(new PlayerLevel(level, 500d)));
		// set higher level index.
		unavailable.getLevels().get(0).setLevelIndex(5);
		playerSrv.saveState(unavailable);

		RequestBuilder builder = null;
		try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/challengers", gameId, "proposer")
					.contentType(MediaType.APPLICATION_JSON);
			mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
					.andExpect(jsonPath("$", hasSize(1)));

		} catch (Exception e) {
			fail("exception thrown: " + e.getMessage());
		}

	}

	@Test
	public void read_system_playersState_three_invitations_test() {
		final String gameId = "PST_GAME";
		Game g = new Game(gameId);
		g.setConcepts(new HashSet<>());
		g.getConcepts().add(new PointConcept("green"));
		g = gameSrv.saveGameDefinition(g);

		Level level = new Level("Eco Warrior", "green");
		level.getThresholds().add(new Threshold("newbie", 0));
		level.getThresholds().add(new Threshold("adept", 100));
		level.getThresholds().add(new Threshold("master", 1000));
		gameSrv.upsertLevel(gameId, level);

		PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
		p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
		playerSrv.saveState(p);

		PlayerState available = new PlayerState(gameId, "av1");
		available.updateLevels(Arrays.asList(new PlayerLevel(level, 400d)));
		playerSrv.saveState(available);

		PlayerState unavailable = new PlayerState(gameId, "unAvailable");
		unavailable.updateLevels(Arrays.asList(new PlayerLevel(level, 500d)));
		// add three invitations in group challenge for user.
		GroupChallenge gc1 = new GroupChallenge();
		Attendee guest = new Attendee();
		guest.setPlayerId("unAvailable");
		guest.setRole(Role.GUEST);
		gc1.getAttendees().add(guest);
		gc1.setState(ChallengeState.PROPOSED);
		gc1.setGameId(gameId);
		mongo.save(gc1);

		GroupChallenge gc2 = new GroupChallenge();
		gc2.getAttendees().add(guest);
		gc2.setState(ChallengeState.PROPOSED);
		gc2.setGameId(gameId);
		mongo.save(gc2);

		GroupChallenge gc3 = new GroupChallenge();
		gc3.getAttendees().add(guest);
		gc3.setState(ChallengeState.PROPOSED);
		gc3.setGameId(gameId);
		mongo.save(gc3);

		playerSrv.saveState(unavailable);

		RequestBuilder builder = null;
		try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/challengers", gameId, "proposer")
					.contentType(MediaType.APPLICATION_JSON);
			mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
					.andExpect(jsonPath("$", hasSize(1)));

		} catch (Exception e) {
			fail("exception thrown: " + e.getMessage());
		}

	}

	@Test
	public void read_system_playersState_groupchallenge_assigned_test() {
		final String gameId = "PST_GAME";
		Game g = new Game(gameId);
		g.setConcepts(new HashSet<>());
		g.getConcepts().add(new PointConcept("green"));
		g = gameSrv.saveGameDefinition(g);

		Level level = new Level("Eco Warrior", "green");
		level.getThresholds().add(new Threshold("newbie", 0));
		level.getThresholds().add(new Threshold("adept", 100));
		level.getThresholds().add(new Threshold("master", 1000));
		gameSrv.upsertLevel(gameId, level);

		PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
		p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
		playerSrv.saveState(p);

		PlayerState available = new PlayerState(gameId, "av1");
		available.updateLevels(Arrays.asList(new PlayerLevel(level, 400d)));
		playerSrv.saveState(available);

		PlayerState unavailable = new PlayerState(gameId, "unAvailable");
		unavailable.updateLevels(Arrays.asList(new PlayerLevel(level, 500d)));
		// assign group challenge in future.
		GroupChallenge gc1 = new GroupChallenge();
		Attendee guest = new Attendee();
		guest.setPlayerId("unAvailable");
		guest.setRole(Role.GUEST);
		gc1.getAttendees().add(guest);
		// set date in future.
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		gc1.setStart(cal.getTime());
		gc1.setState(ChallengeState.ASSIGNED);
		gc1.setGameId(gameId);
		mongo.save(gc1);

		playerSrv.saveState(unavailable);

		RequestBuilder builder = null;
		try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/challengers", gameId, "proposer")
					.contentType(MediaType.APPLICATION_JSON);
			mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
					.andExpect(jsonPath("$", hasSize(1)));

		} catch (Exception e) {
			fail("exception thrown: " + e.getMessage());
		}

	}

    @Ignore // requirement is changed: you can invite a guest with 1 ASSIGNED single challenge
	@Test
	public void read_system_playersState_singlechallenge_assigned_test() {
		final String gameId = "PST_GAME";
		Game g = new Game(gameId);
		g.setConcepts(new HashSet<>());
		g.getConcepts().add(new PointConcept("green"));
		g = gameSrv.saveGameDefinition(g);

		Level level = new Level("Eco Warrior", "green");
		level.getThresholds().add(new Threshold("newbie", 0));
		level.getThresholds().add(new Threshold("adept", 100));
		level.getThresholds().add(new Threshold("master", 1000));
		gameSrv.upsertLevel(gameId, level);

		PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
		p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
		playerSrv.saveState(p);

		PlayerState available = new PlayerState(gameId, "av1");
		available.updateLevels(Arrays.asList(new PlayerLevel(level, 400d)));
		playerSrv.saveState(available);

		PlayerState unavailable = new PlayerState(gameId, "unAvailable");
		unavailable.updateLevels(Arrays.asList(new PlayerLevel(level, 500d)));
		// assign single challenge in future.
		ChallengeConcept singlePlayerChallenge = new ChallengeConcept();
		singlePlayerChallenge.setModelName("absoluteIncrement");
		singlePlayerChallenge.setName("incrementInstance");
		singlePlayerChallenge.getFields().put("difficulty", 2.0);
		singlePlayerChallenge.getFields().put("wi", 10);
		singlePlayerChallenge.getFields().put("bonusScore", 200);
		singlePlayerChallenge.setState(ChallengeState.ASSIGNED);
		// set date in future.
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		singlePlayerChallenge.setStart(cal.getTime());
		unavailable.getState().add(singlePlayerChallenge);

		playerSrv.saveState(unavailable);

		RequestBuilder builder = null;
		try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/challengers", gameId, "proposer")
					.contentType(MediaType.APPLICATION_JSON);
			mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
					.andExpect(jsonPath("$", hasSize(1)));

		} catch (Exception e) {
			fail("exception thrown: " + e.getMessage());
		}

	}

	@Test
	public void read_system_playersState_invalid_input() {
		String expectedErrorMsg1 = "readSystemPlayerState: no player state | empty level found for player no-existing-player for game non-existing-game";
		String expectedErrorMsg2 = "readSystemPlayerState: no reference level found for player proposer for game PST_GAME for conceptName test";

		RequestBuilder builder = MockMvcRequestBuilders
				.get("/data/game/{gameId}/player/{playerId}/systemList", "non-existing-game", "no-existing-player")
				.contentType(MediaType.APPLICATION_JSON);
		try {
			String errorMsg = mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(404))
					.andReturn().getResolvedException().getMessage();
			Assert.assertEquals(errorMsg, expectedErrorMsg1);

			final String gameId = "PST_GAME";

			Game g = new Game(gameId);
			g.setConcepts(new HashSet<>());
			g.getConcepts().add(new PointConcept("green"));
			gameSrv.saveGameDefinition(g);

			Level level = new Level("Eco Warrior", "green");
			level.getThresholds().add(new Threshold("newbie", 0));
			level.getThresholds().add(new Threshold("adept", 100));
			level.getThresholds().add(new Threshold("master", 1000));
			gameSrv.upsertLevel(gameId, level);

			PlayerState p = playerSrv.loadState(gameId, "proposer", true, false);
			p.updateLevels(Arrays.asList(new PlayerLevel(level, 300d)));
			playerSrv.saveState(p);

			builder = MockMvcRequestBuilders.get("/data/game/{gameId}/player/{playerId}/systemList", gameId, "proposer")
					.param("conceptName", "test").contentType(MediaType.APPLICATION_JSON);

			String errorMsg2 = mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(404))
					.andReturn().getResolvedException().getMessage();
			Assert.assertEquals(errorMsg2, expectedErrorMsg2);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
    
    private Date date(String isoDate) {
        return LocalDateTime.parse(isoDate).toDate();
    }
}
