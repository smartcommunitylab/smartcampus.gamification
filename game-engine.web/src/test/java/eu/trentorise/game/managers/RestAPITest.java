/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.trentorise.game.managers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.bean.ExecutionDataDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.NoSecurityConfig;
import eu.trentorise.game.config.WebConfig;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.GeneralClassificationTask;

/**
 * 
 * Actually execution tests use Thread.sleep to wait QueueGameWorkflow async conclusion. This MUST
 * to be fixed, because test result can be machine dependent.
 * 
 * 
 * @author mirko perillo
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("no-sec")
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, NoSecurityConfig.class,
        TestMVCConfiguration.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class RestAPITest {

    @Autowired
    private GameManager gameManager;

    @Autowired
    private PlayerService playerSrv;

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

    @Test
    public void gameEndedRest() throws JsonProcessingException {
        GamePersistence gp = defineGame();
        gp.setDomain(DOMAIN);
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.HOUR_OF_DAY, -2);
        gp.setExpiration(cal.getTimeInMillis());
        mongo.save(gp);
        gameManager.taskDestroyer();

        ExecutionDataDTO bean = new ExecutionDataDTO();
        bean.setActionId(ACTION);
        bean.setPlayerId("1");
        bean.setGameId(gp.getId());
        RequestBuilder builder = MockMvcRequestBuilders.post("/gengine/execute/{domain}", DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bean));
        try {
            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(403));
        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void createPlayer() {
        GamePersistence gp = defineGame();
        mongo.save(gp);
        PlayerStateDTO player = new PlayerStateDTO();
        player.setPlayerId("play1");
        CustomData c = new CustomData();
        c.put("playername", "sid");
        c.put("level", 21);
        player.setCustomData(c);

        Assert.assertNull(playerSrv.loadState(GAME, "play1", false));

        try {
            RequestBuilder builder = MockMvcRequestBuilders
                    .post("/console/game/{domain}/{game}/player", DOMAIN, GAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(player));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState play = playerSrv.loadState(GAME, "play1", false);
            Assert.assertNotNull(play);
            Assert.assertEquals(21, play.getCustomData().get("level"));

            builder =
                    MockMvcRequestBuilders.delete("/console/game/{domain}/{game}/player/{playerId}",
                            DOMAIN, GAME, play.getPlayerId());

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }

        Assert.assertNull(playerSrv.loadState(GAME, "play1", false));
    }

    @Test
    public void updatePlayer() {
        GamePersistence gp = defineGame();
        mongo.save(gp);
        PlayerState player = new PlayerState(GAME, "play1");
        CustomData c = new CustomData();
        c.put("playername", "sid");
        c.put("level", 21);
        player.setCustomData(c);

        playerSrv.saveState(player);

        Map<String, Object> updateData = new HashMap<String, Object>();
        updateData.put("newData", "data");

        try {
            RequestBuilder builder = MockMvcRequestBuilders
                    .put("/console/game/{domain}/{game}/player/{playerId}", DOMAIN, GAME,
                            player.getPlayerId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(updateData));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState play = playerSrv.loadState(GAME, "play1", false);
            Assert.assertNotNull(play);
            Assert.assertEquals("data", play.getCustomData().get("newData"));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }

    }

    @Test
    public void playerAlreadyExist() {
        GamePersistence gp = defineGame();
        mongo.save(gp);
        PlayerStateDTO player = new PlayerStateDTO();
        player.setPlayerId("play1");

        Assert.assertNull(playerSrv.loadState(GAME, "play1", false));

        try {
            RequestBuilder builder = MockMvcRequestBuilders
                    .post("/console/game/{domain}/{game}/player", DOMAIN, GAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(player));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            // try to recreate player
            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void teamAPI() {
        GamePersistence gp = defineGame();
        mongo.save(gp);
        TeamDTO t = new TeamDTO();
        t.setGameId(GAME);
        t.setName("muppet");
        t.setMembers(Arrays.asList("p1", "p2", "p3"));
        t.setPlayerId("t1");
        CustomData c = new CustomData();
        c.put("level", "hunter");
        t.setCustomData(c);

        Assert.assertNull(playerSrv.readTeam(GAME, "t1"));

        try {
            RequestBuilder builder = MockMvcRequestBuilders
                    .post("/console/game/{domain}/{gameId}/team", DOMAIN, GAME)
                    .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            TeamState team = playerSrv.readTeam(GAME, "t1");
            Assert.assertNotNull(team);
            Assert.assertEquals("hunter", team.getCustomData().get("level"));
            Assert.assertArrayEquals(new String[] {"p1", "p2", "p3"},
                    team.getMembers().toArray(new String[0]));
            Assert.assertNotNull(team.getState());
            Assert.assertEquals(3, team.getState().size());

            builder = MockMvcRequestBuilders
                    .post("/console/game/{domain}/{game}/team/{teamId}/members", DOMAIN, GAME,
                            team.getPlayerId())
                    .contentType(MediaType.APPLICATION_JSON).content("[\"p20\",\"p22\"]");

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            team = playerSrv.readTeam(GAME, "t1");
            Assert.assertNotNull(team);
            Assert.assertEquals("hunter", team.getCustomData().get("level"));
            Assert.assertArrayEquals(new String[] {"p20", "p22"},
                    team.getMembers().toArray(new String[0]));

            builder = MockMvcRequestBuilders.delete("/console/game/{domain}/{game}/team/{teamId}",
                    DOMAIN, GAME, team.getPlayerId());

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            Assert.assertNull(playerSrv.readTeam(GAME, "t1"));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }

    }

    @Test
    public void getTeamsByMember() {
        GamePersistence gp = defineGame();
        mongo.save(gp);

        playerSrv.saveState(new PlayerState(GAME, "p1"));
        playerSrv.saveState(new PlayerState(GAME, "p2"));

        TeamState t = new TeamState(GAME, "t1");
        t.getMembers().add("p1");
        t.getMembers().add("p2");
        playerSrv.saveTeam(t);

        t = new TeamState(GAME, "t2");
        t.getMembers().add("p2");
        playerSrv.saveTeam(t);

        t = new TeamState(GAME, "t3");
        t.getMembers().add("p1");
        playerSrv.saveTeam(t);

        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders.get(
                    "/console/game/{domain}/{game}/player/{playerId}/teams", DOMAIN, GAME, "p1");
            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].playerId", Matchers.is("t3")));
        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }

    }

    @Test
    public void readAPI() {
        GamePersistence gp = defineGame();
        mongo.save(gp);
        PlayerState p = new PlayerState(GAME, "p1");
        p.getCustomData().put("myattr", "I'm the winner");
        playerSrv.saveState(p);

        TeamState team = new TeamState(GAME, "t1");
        team.setMembers(Arrays.asList("p1"));
        playerSrv.saveTeam(team);

        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders.get("/gengine/state/{domain}/{gameId}/{playerId}",
                    DOMAIN, GAME, p.getPlayerId());
            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.playerId", Matchers.is("p1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.customData.myattr",
                            Matchers.is("I\'m the winner")));

            builder = MockMvcRequestBuilders.get("/gengine/state/{domain}/{gameId}", DOMAIN, GAME);
            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].playerId",
                            Matchers.is("p1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].customData.myattr",
                            Matchers.is("I\'m the winner")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].state",
                            Matchers.notNullValue()));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }

    }

    private GamePersistence defineGame() {
        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);
        game.getActions().add("classification");

        game.setConcepts(new HashSet<GameConcept>());
        game.getConcepts().add(new PointConcept("points"));
        game.getConcepts().add(new PointConcept("scores"));
        game.getConcepts().add(new BadgeCollectionConcept("badges"));

        game.setTasks(new HashSet<GameTask>());

        // final classifications
        TaskSchedule schedule = new TaskSchedule();
        schedule.setCronExpression("0 20 * * * *");
        GeneralClassificationTask task1 = new GeneralClassificationTask(schedule, 3, "green leaves",
                "final classification green");
        game.getTasks().add(task1);

        // schedule = new TaskSchedule(); //
        schedule.setCronExpression("0 * * * * *");
        GeneralClassificationTask task2 =
                new GeneralClassificationTask(schedule, 3, "health", "final classification health");
        game.getTasks().add(task2);

        // schedule = new TaskSchedule(); //
        schedule.setCronExpression("0 * * * * *");
        GeneralClassificationTask task3 =
                new GeneralClassificationTask(schedule, 3, "p+r", "final classification p+r");
        game.getTasks().add(task3);

        // week classifications // schedule = new TaskSchedule(); //
        schedule.setCronExpression("0 * * * * *");
        GeneralClassificationTask task4 = new GeneralClassificationTask(schedule, 1, "green leaves",
                "week classification green");
        game.getTasks().add(task4);

        // schedule = new TaskSchedule(); //
        schedule.setCronExpression("0 * * * * *");
        GeneralClassificationTask task5 =
                new GeneralClassificationTask(schedule, 1, "health", "week classification health");
        game.getTasks().add(task5);

        // schedule = new TaskSchedule(); //
        schedule.setCronExpression("0 * * * * *");
        GeneralClassificationTask task6 =
                new GeneralClassificationTask(schedule, 1, "p+r", "week classification p+r");
        game.getTasks().add(task6);

        return new GamePersistence(game);

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
