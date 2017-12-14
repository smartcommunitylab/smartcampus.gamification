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
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.services.GameService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, TestMVCConfiguration.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class GameControllerTest {

    private MockMvc mocker;

    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongo;

    private static final String DOMAIN = "my-domain";

    @Before
    public void cleanDB() {
        mongo.getDb().dropDatabase();
    }

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private GameService gameSrv;



    @PostConstruct
    public void init() {
        mocker = MockMvcBuilders.webAppContextSetup(wac).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void create_a_new_game() throws JsonProcessingException {
        GameDTO game = new GameDTO();
        game.setDomain("fake_domain");
        final String gameName = "the game";
        game.setName(gameName);
        Set<String> actions = new HashSet<>();
        actions.add("action1");
        game.setActions(actions);

        RequestBuilder builder = MockMvcRequestBuilders.post("/model/game/{domain}", DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(game));

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                    .andExpect(jsonPath("$.domain", is(DOMAIN)))
                    .andExpect(jsonPath("$.name", is(gameName)))
                    .andExpect(jsonPath("$.actions", contains("action1")));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    @Test
    public void read_a_game() {
        Game g = new Game();
        g.setDomain(DOMAIN);
        g.setOwner("sco_master");
        g.setName("the game");
        Set<String> actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        g = gameSrv.saveGameDefinition(g);

        RequestBuilder builder =
                MockMvcRequestBuilders.get("/model/game/{domain}/{gameId}", DOMAIN, g.getId());

        try {
            mocker.perform(builder).andExpect(status().is(200))
                    .andExpect(jsonPath("$.id", is(g.getId())))
                    .andExpect(jsonPath("$.domain", is(DOMAIN)))
                    .andExpect(jsonPath("$.name", is(g.getName())))
                    .andExpect(jsonPath("$.actions", contains("a1", "a2")));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    @Test
    public void read_all_user_game() {
        Game g = new Game();
        g.setDomain(DOMAIN);
        g.setOwner("sco_master");
        g.setName("the game");
        Set<String> actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        gameSrv.saveGameDefinition(g);

        g = new Game();
        g.setDomain(DOMAIN);
        g.setOwner("sco_master");
        g.setName("the new game");
        actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        gameSrv.saveGameDefinition(g);


        RequestBuilder builder = MockMvcRequestBuilders.get("/model/game/{domain}", DOMAIN);

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
        g.setDomain(DOMAIN);
        g.setOwner("sco_master");
        g.setName("the game");
        Set<String> actions = new HashSet<>();
        actions.add("a1");
        actions.add("a2");
        g.setActions(actions);
        g = gameSrv.saveGameDefinition(g);

        RequestBuilder builder =
                MockMvcRequestBuilders.delete("/model/game/{domain}/{gameId}", DOMAIN, g.getId());

        try {
            mocker.perform(builder).andExpect(status().is(200));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

        // check if the game has been deleted
        Assert.assertNull(gameSrv.loadGameDefinitionById(g.getId()));
    }
}
