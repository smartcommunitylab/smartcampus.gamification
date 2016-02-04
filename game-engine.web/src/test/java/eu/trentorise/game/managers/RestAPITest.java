/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.managers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

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
import eu.trentorise.game.config.WebConfig;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.ClassificationTask;

/**
 * 
 * Actually execution tests use Thread.sleep to wait QueueGameWorkflow async
 * conclusion. This MUST to be fixed, because test result can be machine
 * dependent.
 * 
 * 
 * @author mirko perillo
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class,
		TestMVCConfiguration.class }, loader = AnnotationConfigWebContextLoader.class)
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

	private static final String GAME = "gameTest";
	private static final String ACTION = "save_itinerary";

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
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.HOUR_OF_DAY, -2);
		gp.setExpiration(cal.getTimeInMillis());
		mongo.save(gp);
		gameManager.taskDestroyer();

		mocker = MockMvcBuilders.webAppContextSetup(wac).build();
		ObjectMapper mapper = new ObjectMapper();
		ExecutionDataDTO bean = new ExecutionDataDTO();
		bean.setActionId(ACTION);
		bean.setUserId("1");
		bean.setGameId(gp.getId());
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/gengine/execute")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(bean));
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

		mocker = MockMvcBuilders.webAppContextSetup(wac).build();
		ObjectMapper mapper = new ObjectMapper();
		Assert.assertNull(playerSrv.loadState(GAME, "play1", false));

		try {
			RequestBuilder builder = MockMvcRequestBuilders
					.post("/console/game/" + GAME + "/player")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(player));

			mocker.perform(builder).andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().is(200));

			PlayerState play = playerSrv.loadState(GAME, "play1", false);
			Assert.assertNotNull(play);
			Assert.assertEquals(21, play.getCustomData().get("level"));

			builder = MockMvcRequestBuilders.delete("/console/game/" + GAME
					+ "/player/" + play.getPlayerId());

			mocker.perform(builder).andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().is(200));

		} catch (Exception e) {
			Assert.fail("exception " + e.getMessage());
		}

		Assert.assertNull(playerSrv.loadState(GAME, "play1", false));
	}

	@Test
	public void playerAlreadyExist() {
		GamePersistence gp = defineGame();
		mongo.save(gp);
		PlayerStateDTO player = new PlayerStateDTO();
		player.setPlayerId("play1");

		mocker = MockMvcBuilders.webAppContextSetup(wac).build();
		ObjectMapper mapper = new ObjectMapper();
		Assert.assertNull(playerSrv.loadState(GAME, "play1", false));

		try {
			RequestBuilder builder = MockMvcRequestBuilders
					.post("/console/game/" + GAME + "/player")
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

		mocker = MockMvcBuilders.webAppContextSetup(wac).build();
		ObjectMapper mapper = new ObjectMapper();
		Assert.assertNull(playerSrv.readTeam(GAME, "t1"));

		try {
			RequestBuilder builder = MockMvcRequestBuilders
					.post("/console/game/" + GAME + "/team")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(t));

			mocker.perform(builder).andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().is(200));

			Team team = playerSrv.readTeam(GAME, "t1");
			Assert.assertNotNull(team);
			Assert.assertEquals("hunter", team.getCustomData().get("level"));
			Assert.assertArrayEquals(new String[] { "p1", "p2", "p3" }, team
					.getMembers().toArray(new String[0]));

			builder = MockMvcRequestBuilders.delete("/console/game/" + GAME
					+ "/team/" + team.getPlayerId());

			mocker.perform(builder).andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().is(200));

			Assert.assertNull(playerSrv.readTeam(GAME, "t1"));

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

		game.setTasks(new HashSet<GameTask>());

		// final classifications
		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("0 20 * * * *");
		ClassificationTask task1 = new ClassificationTask(schedule, 3,
				"green leaves", "final classification green");
		game.getTasks().add(task1);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task2 = new ClassificationTask(schedule, 3,
				"health", "final classification health");
		game.getTasks().add(task2);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task3 = new ClassificationTask(schedule, 3, "p+r",
				"final classification p+r");
		game.getTasks().add(task3);

		// week classifications // schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task4 = new ClassificationTask(schedule, 1,
				"green leaves", "week classification green");
		game.getTasks().add(task4);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task5 = new ClassificationTask(schedule, 1,
				"health", "week classification health");
		game.getTasks().add(task5);

		// schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 * * * * *");
		ClassificationTask task6 = new ClassificationTask(schedule, 1, "p+r",
				"week classification p+r");
		game.getTasks().add(task6);

		return new GamePersistence(game);

	}

}

/**
 * Without @EnablaWebMvc MockMvc not work correctly to simulate controller
 * Cannot add annotation to WebConfig to conflict with WebMvcConfigurerAdapter
 * extension
 * 
 * @author mirko perillo
 * 
 */
@EnableWebMvc
class TestMVCConfiguration extends WebConfig {

}
