package eu.trentorise.game.api.rest;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.LevelDTO;
import eu.trentorise.game.bean.LevelDTO.ThresholdDTO;
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.NoSecurityConfig;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"no-sec"})
@ContextConfiguration(
        classes = {AppConfig.class, MongoConfig.class, NoSecurityConfig.class,
                TestMVCConfiguration.class,
},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class GameControllerTest {

    private static final String ROLE = "ADMIN";

    private MockMvc mocker;

    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;


    @Before
    public void cleanDB() {
        mongo.getDb().dropDatabase();
    }




    @PostConstruct
    public void init() {
        mocker = MockMvcBuilders.webAppContextSetup(wac).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void create_a_new_game() throws JsonProcessingException {
        GameDTO game = new GameDTO();
        final String gameName = "the game";
        game.setName(gameName);
        Set<String> actions = new HashSet<>();
        actions.add("action1");
        game.setActions(actions);

        RequestBuilder builder = MockMvcRequestBuilders.post("/model/game")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(game));

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                    .andExpect(jsonPath("$.name", is(gameName)))
                    .andExpect(jsonPath("$.actions", contains("action1")));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    @Test
    public void read_a_game() {
        Game g = new Game();
        g.setName("the game");
        Set<String> actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        g = gameSrv.saveGameDefinition(g);

        RequestBuilder builder =
                MockMvcRequestBuilders.get("/model/game/{gameId}", g.getId());

        try {
            mocker.perform(builder).andExpect(status().is(200))
                    .andExpect(jsonPath("$.id", is(g.getId())))
                    .andExpect(jsonPath("$.name", is(g.getName())))
                    .andExpect(jsonPath("$.actions", contains("a1", "a2")));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    @Test
    public void read_all_user_game() {
        Game g = new Game();
        g.setOwner("sco_master");
        g.setName("the game");
        Set<String> actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        gameSrv.saveGameDefinition(g);

        g = new Game();
        g.setOwner("sco_master");
        g.setName("the new game");
        actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        gameSrv.saveGameDefinition(g);


        RequestBuilder builder = MockMvcRequestBuilders.get("/model/game");

        try {
            mocker.perform(builder).andExpect(status().is(200))
                    .andExpect(jsonPath("$", hasSize(2)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }

    @Test
    public void delete_a_game() {

        Game g = new Game();
        g.setOwner("sco_master");
        g.setName("the game");
        Set<String> actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        g = gameSrv.saveGameDefinition(g);

        RequestBuilder builder =
                MockMvcRequestBuilders.delete("/model/game/{gameId}", g.getId());

        try {
            mocker.perform(builder).andExpect(status().is(200));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

        // check if the game has been deleted
        Assert.assertNull(gameSrv.loadGameDefinitionById(g.getId()));
    }

    @Test
    public void define_a_level() throws JsonProcessingException {
        final String gameId = "MY_GAME";
        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g = gameSrv.saveGameDefinition(g);

        LevelDTO level = new LevelDTO();
        level.setName("Eco Warrior");
        level.setPointConceptName("green");

        level.getThresholds().add(threshold("newbie", 0));
        level.getThresholds().add(threshold("adept", 100));
        level.getThresholds().add(threshold("master", 1000));

        String levelAsString = mapper.writeValueAsString(level);

        RequestBuilder builder = MockMvcRequestBuilders.post("/model/game/{gameId}/level", gameId)
                .contentType(MediaType.APPLICATION_JSON).content(levelAsString);

        try {
            mocker.perform(builder).andExpect(status().is(200))
                    .andExpect(jsonPath("$.name", is("Eco Warrior")))
                    .andExpect(jsonPath("$.thresholds", hasSize(3)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

        Game loaded = gameSrv.loadGameDefinitionById(gameId);
        Assert.assertEquals(1, loaded.getLevels().size());
        Level loadedLevel = loaded.getLevels().get(0);
        Assert.assertEquals("Eco Warrior", loadedLevel.getName());
        Assert.assertEquals(3, loadedLevel.getThresholds().size());
        Threshold newbie = loadedLevel.getThresholds().get(0);
        Assert.assertEquals("newbie", newbie.getName());

    }



    @Test
    public void delete_a_level() throws JsonProcessingException {
        final String gameId = "MY_GAME";
        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g = gameSrv.saveGameDefinition(g);

        LevelDTO level = new LevelDTO();
        level.setName("Eco Warrior");
        level.setPointConceptName("green");

        level.getThresholds().add(threshold("newbie", 0));
        level.getThresholds().add(threshold("adept", 100));
        level.getThresholds().add(threshold("master", 1000));

        String levelAsString = mapper.writeValueAsString(level);

        RequestBuilder builder = MockMvcRequestBuilders.post("/model/game/{gameId}/level", gameId)
                .contentType(MediaType.APPLICATION_JSON).content(levelAsString);

        try {
            mocker.perform(builder).andExpect(status().is(200))
                    .andExpect(jsonPath("$.name", is("Eco Warrior")))
                    .andExpect(jsonPath("$.thresholds", hasSize(3)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

        RequestBuilder deleteBuilder = MockMvcRequestBuilders
                .delete("/model/game/{gameId}/level/{levelName}", gameId, "Eco Warrior");

        try {
            mocker.perform(deleteBuilder).andExpect(status().is(200));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

        Game loaded = gameSrv.loadGameDefinitionById(gameId);
        Assert.assertEquals(0, loaded.getLevels().size());

    }


    @Test
    public void read_player_level() throws JsonProcessingException {
        final String gameId = "MY_GAME";
        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g = gameSrv.saveGameDefinition(g);

        Level level = new Level("Eco Warrior", "green");
        level.getThresholds().add(new Threshold("newbie", 0));
        level.getThresholds().add(new Threshold("adept", 100));
        level.getThresholds().add(new Threshold("master", 1000));

        gameSrv.upsertLevel(gameId, level);

        PlayerState p = playerSrv.loadState(gameId, "player", true);
        p.getLevels().add(new PlayerLevel(level, 300d));
        playerSrv.saveState(p);
        
        RequestBuilder getBuilder = MockMvcRequestBuilders
                .get("/data/game/{gameId}/player/{playerId}/levels", gameId, "player");

        try {
            mocker.perform(getBuilder).andExpect(status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].levelName", is("Eco Warrior")))
                    .andExpect(jsonPath("$[0].levelValue", is("adept")))
                    .andExpect(jsonPath("$[0].pointConcept", is("green")))
                    .andExpect(jsonPath("$[0].startLevelScore", is(100d)))
                    .andExpect(jsonPath("$[0].endLevelScore", is(1000d)))
                    .andExpect(jsonPath("$[0].toNextLevel", is(700d)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }


    private ThresholdDTO threshold(String name, double value) {
        ThresholdDTO thres = new ThresholdDTO();
        thres.setName(name);
        thres.setValue(value);

        return thres;
    }
}
