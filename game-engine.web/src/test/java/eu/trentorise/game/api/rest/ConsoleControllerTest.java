package eu.trentorise.game.api.rest;

import java.util.HashSet;

import javax.annotation.PostConstruct;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.NoSecurityConfig;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("no-sec")
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, NoSecurityConfig.class,
        TestMVCConfiguration.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class ConsoleControllerTest {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mocker;

    private ObjectMapper mapper;

    private static final String GAME = "gameTest";
    private static final String ACTION = "save_itinerary";
    private static final String DOMAIN = "my-domain";

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
                    .post("/console/game/{domain}/{gameId}/player", DOMAIN, game.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(player));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }
}
