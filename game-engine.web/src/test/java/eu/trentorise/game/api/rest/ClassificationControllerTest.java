package eu.trentorise.game.api.rest;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.WebConfig;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.GeneralClassificationTask;
import eu.trentorise.game.task.IncrementalClassificationTask;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, TestMVCConfiguration.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class ClassificationControllerTest {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mocker;

    private ObjectMapper mapper;

    private static final String GAME = "gameTest";
    private static final String DOMAIN = "my-domain";
    private static final String ACTION = "save_itinerary";
    private static final String GEN_CLASSIFICATION_NAME = "nuova classifica settimanale";
    private static final String INC_CLASSIFICATION_NAME =
            "nuova classifica settimanale incremEntalE";

    private static final String POINT_CONCEPT = "green leaves";
    private static final String PERIOD_NAME = "period1";

    @PostConstruct
    public void init() {
        mocker = MockMvcBuilders.webAppContextSetup(wac).build();
        mapper = new ObjectMapper();
    }

    @Before
    public void cleanDB() {
        // clean mongo
        mongo.dropCollection(StatePersistence.class);
        mongo.dropCollection(GamePersistence.class);
        mongo.dropCollection(NotificationPersistence.class);
    }

    private Game defineGame() {
        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);
        game.setDomain(DOMAIN);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);
        game.getActions().add("classification");

        game.setConcepts(new HashSet<GameConcept>());

        PointConcept green = new PointConcept(POINT_CONCEPT);
        green.addPeriod(PERIOD_NAME, new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);

        game.getConcepts().add(green);

        game.setTasks(new HashSet<GameTask>());

        GeneralClassificationTask genClass = new GeneralClassificationTask();
        genClass.setName(GEN_CLASSIFICATION_NAME);
        genClass.setItemType(POINT_CONCEPT);
        genClass.setItemsToNotificate(3);
        game.getTasks().add(genClass);

        IncrementalClassificationTask incClass = new IncrementalClassificationTask();
        incClass.setName(INC_CLASSIFICATION_NAME);
        incClass.setPointConceptName(POINT_CONCEPT);
        incClass.setPeriodName(PERIOD_NAME);
        incClass.setItemsToNotificate(3);
        game.getTasks().add(incClass);

        return game;
    }

    @Test
    public void incrementalClassification() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        PlayerState state = new PlayerState(GAME, "player1");
        PointConcept green = new PointConcept(POINT_CONCEPT);
        green.addPeriod(PERIOD_NAME, new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(10d);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player2");
        green = new PointConcept(POINT_CONCEPT);
        green.addPeriod(PERIOD_NAME, new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(12d);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player3");
        green = new PointConcept(POINT_CONCEPT);
        green.addPeriod(PERIOD_NAME, new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(4d);
        state.getState().add(green);
        playerSrv.saveState(state);
        try {
            RequestBuilder builder = MockMvcRequestBuilders.get(
                    "/data/game/{domain}/{game}/incclassification/{classificationName}", DOMAIN,
                    GAME, INC_CLASSIFICATION_NAME);

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void incrementalClassificationPaginationTest() {
        Game game = defineGame();

        PlayerState state = new PlayerState(GAME, "player1");
        PointConcept green = new PointConcept(POINT_CONCEPT);
        green.addPeriod(PERIOD_NAME, new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(10d);
        game.getConcepts().add(green);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player2");
        green = new PointConcept(POINT_CONCEPT);
        green.addPeriod(PERIOD_NAME, new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(12d);
        game.getConcepts().add(green);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player3");
        green = new PointConcept(POINT_CONCEPT);
        green.addPeriod(PERIOD_NAME, new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(4d);
        game.getConcepts().add(green);
        state.getState().add(green);
        playerSrv.saveState(state);

        gameSrv.saveGameDefinition(game);
        try {
            /** using period index. **/
            RequestBuilder builder = MockMvcRequestBuilders
                    .get("/data/game/{domain}/{game}/incclassification/{classificationName}",
                            DOMAIN, GAME, INC_CLASSIFICATION_NAME)
                    .param("periodInstanceIndex", "1");

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }

        /** test pagination using time stamp. **/
        try {
            RequestBuilder builderP = MockMvcRequestBuilders
                    .get("/data/game/{domain}/{game}/incclassification/{classificationName}",
                            DOMAIN, GAME, INC_CLASSIFICATION_NAME)
                    .param("timestamp", String.valueOf(System.currentTimeMillis()))
                    .param("page", "1").param("size", "1");

            MvcResult response = mocker.perform(builderP).andReturn();
            JSONParser parser = new JSONParser(-1);
            JSONObject classificaitonBoard =
                    (JSONObject) parser.parse(response.getResponse().getContentAsString());
            JSONArray board = (JSONArray) classificaitonBoard.get("board");
            Assert.assertEquals(board.size(), 1);

            /** test sorting result. **/
            JSONObject score = (JSONObject) board.get(0);
            Assert.assertEquals(score.get("score"), 12d);

            /** test pagination. **/
            builderP = MockMvcRequestBuilders
                    .get("/data/game/{domain}/{game}/incclassification/{classificationName}",
                            DOMAIN, GAME, INC_CLASSIFICATION_NAME)
                    .param("timestamp", String.valueOf(System.currentTimeMillis()))
                    .param("page", "2").param("size", "5");

            response = mocker.perform(builderP).andReturn();
            classificaitonBoard =
                    (JSONObject) parser.parse(response.getResponse().getContentAsString());
            board = (JSONArray) classificaitonBoard.get("board");
            Assert.assertEquals(board.size(), 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void defineIncrementalOK() {
        try {
            Game game = defineGame();
            gameSrv.saveGameDefinition(game);
            IncrementalClassificationDTO incrClassDTO = new IncrementalClassificationDTO();
            incrClassDTO.setClassificationName("test");
            incrClassDTO.setItemsToNotificate(2);
            incrClassDTO.setItemType(POINT_CONCEPT);
            incrClassDTO.setPeriodName(PERIOD_NAME);
            incrClassDTO.setGameId(GAME);

            RequestBuilder builder = MockMvcRequestBuilders
                    .post("/model/game/{domain}/{game}/incclassification", DOMAIN, GAME)
                    .header("Content-Type", "application/json")
                    .content(mapper.writeValueAsString(incrClassDTO));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void defineIncrementalNoGameId() {
        try {
            Game game = defineGame();
            gameSrv.saveGameDefinition(game);
            IncrementalClassificationDTO incrClassDTO = new IncrementalClassificationDTO();
            incrClassDTO.setClassificationName("test");
            incrClassDTO.setItemsToNotificate(2);
            incrClassDTO.setItemType(POINT_CONCEPT);
            incrClassDTO.setPeriodName(PERIOD_NAME);

            RequestBuilder builder = MockMvcRequestBuilders
                    .post("/model/game/{domain}/{game}/incclassification", DOMAIN, GAME)
                    .header("Content-Type", "application/json")
                    .content(mapper.writeValueAsString(incrClassDTO));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void generalClassification() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        PlayerState state = new PlayerState(GAME, "player1");
        PointConcept green = new PointConcept("green leaves");
        green.addPeriod("period1", new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(10d);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player2");
        green = new PointConcept("green leaves");
        green.addPeriod("period1", new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(12d);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player3");
        green = new PointConcept("green leaves");
        green.addPeriod("period1", new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(4d);
        state.getState().add(green);
        playerSrv.saveState(state);
        try {
            RequestBuilder builder = MockMvcRequestBuilders.get(
                    "/data/game/{domain}/{game}/classification/{classificationName}", DOMAIN, GAME,
                    GEN_CLASSIFICATION_NAME);

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void generalClassificationPaginationTest() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        PlayerState state = new PlayerState(GAME, "player1");
        PointConcept green = new PointConcept("green leaves");
        green.addPeriod("period1", new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(10d);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player2");
        green = new PointConcept("green leaves");
        green.addPeriod("period1", new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(12d);
        state.getState().add(green);
        playerSrv.saveState(state);

        state = new PlayerState(GAME, "player3");
        green = new PointConcept("green leaves");
        green.addPeriod("period1", new LocalDate().minusDays(1).toDate(), 24 * 60 * 60000);
        green.setScore(4d);
        state.getState().add(green);
        playerSrv.saveState(state);
        try {
            RequestBuilder builder = MockMvcRequestBuilders.get(
                    "/data/game/{domain}/{game}/classification/{classificationName}", DOMAIN, GAME,
                    GEN_CLASSIFICATION_NAME);

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }

        /** test pagination. **/
        try {
            RequestBuilder builderP =
                    MockMvcRequestBuilders
                            .get("/data/game/{domain}/{game}/classification/{classificationName}",
                                    DOMAIN, GAME, GEN_CLASSIFICATION_NAME)
                            .param("page", "1").param("size", "2");

            MvcResult response = mocker.perform(builderP).andReturn();
            JSONParser parser = new JSONParser(-1);
            JSONObject classificaitonBoard =
                    (JSONObject) parser.parse(response.getResponse().getContentAsString());
            JSONArray board = (JSONArray) classificaitonBoard.get("board");
            Assert.assertEquals(board.size(), 2);

            /** test sorting result. **/
            JSONObject score = (JSONObject) board.get(0);
            Assert.assertEquals(score.get("score"), 12d);

            builderP =
                    MockMvcRequestBuilders
                            .get("/data/game/{domain}/{game}/classification/{classificationName}",
                                    DOMAIN, GAME, GEN_CLASSIFICATION_NAME)
                            .param("page", "2").param("size", "5");

            response = mocker.perform(builderP).andReturn();
            classificaitonBoard =
                    (JSONObject) parser.parse(response.getResponse().getContentAsString());
            board = (JSONArray) classificaitonBoard.get("board");

            Assert.assertEquals(board.size(), 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}


/**
 * Without @EnablaWebMvc MockMvc not work correctly to simulate controller Cannot add annotation to
 * WebConfig to conflict with WebMvcConfigurerAdapter extension
 * 
 * @author mirko perillo
 * 
 */
@EnableWebMvc
class TestMVCConfiguration extends WebConfig {

}
