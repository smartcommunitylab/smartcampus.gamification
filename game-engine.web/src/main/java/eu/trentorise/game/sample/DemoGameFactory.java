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

package eu.trentorise.game.sample;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.ClassificationTask;

@Component
public class DemoGameFactory {

	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(DemoGameFactory.class);

	@Autowired
	private GameService gameSrv;

	private static final String DEMO_GAME = "demo-game";
	private static final String GAME_NAME = "demo-game";
	private static final String GAME_OWNER = "sco_master";

	private static final String WEEK_CLASS_CRONEXP = "0 0/15 * * * *";
	private static final String FINAL_CLASS_CRONEXP = "0 5/15 * * * *";

	public Game createGame(String gameId, String gameName, String gameOwner) {
		if (gameName == null) {
			gameName = GAME_NAME;
		}

		if (gameOwner == null) {
			gameOwner = GAME_OWNER;
		}
		List<Game> g = gameSrv.loadGameByOwner(gameOwner);
		boolean gameFound = false;
		for (Game game : g) {
			if (gameFound = game.getName().equals(gameName)) {
				break;
			}
		}
		if (gameFound) {
			logger.info("sample {} already loaded for user {}", gameName,
					gameOwner);
			return null;
		} else {
			logger.info("sample {} not loaded for user {}..start loading",
					gameName, gameOwner);
			Game game = new Game(gameId);
			game.setName(gameName);
			game.setOwner(gameOwner);

			game.setActions(new HashSet<String>(Arrays.asList("save_itinerary",
					"classification")));

			game.setConcepts(new HashSet<GameConcept>(Arrays.asList(
					new PointConcept("green leaves"),
					new PointConcept("health"), new PointConcept("p+r"),
					new BadgeCollectionConcept("green leaves"),
					new BadgeCollectionConcept("health"),
					new BadgeCollectionConcept("p+r"),
					new BadgeCollectionConcept("special"))));

			// add tasks
			game.setTasks(new HashSet<GameTask>());

			// final classifications
			TaskSchedule weekClassSchedule = new TaskSchedule();
			weekClassSchedule.setCronExpression(WEEK_CLASS_CRONEXP);

			TaskSchedule finalClassSchedule = new TaskSchedule();
			finalClassSchedule.setCronExpression(FINAL_CLASS_CRONEXP);

			ClassificationTask task1 = new ClassificationTask(
					finalClassSchedule, 3, "green leaves",
					"final classification green");
			game.getTasks().add(task1);

			ClassificationTask task2 = new ClassificationTask(
					finalClassSchedule, 3, "health",
					"final classification health");
			game.getTasks().add(task2);

			ClassificationTask task3 = new ClassificationTask(
					finalClassSchedule, 3, "p+r", "final classification p+r");
			game.getTasks().add(task3);

			// week classifications
			ClassificationTask task4 = new ClassificationTask(
					weekClassSchedule, 1, "green leaves",
					"week classification green");
			game.getTasks().add(task4);

			ClassificationTask task5 = new ClassificationTask(
					weekClassSchedule, 1, "health",
					"week classification health");
			game.getTasks().add(task5);

			ClassificationTask task6 = new ClassificationTask(
					weekClassSchedule, 1, "p+r", "week classification p+r");
			game.getTasks().add(task6);

			game = gameSrv.saveGameDefinition(game);
			gameId = game != null ? game.getId() : null;

			// add rules
			try {

				String c = FileUtils.readFileToString(new File(Thread
						.currentThread().getContextClassLoader()
						.getResource("rules/" + DEMO_GAME + "/constants")
						.getFile()));
				DBRule rule = new DBRule(gameId, c);
				rule.setName("constants");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(Thread.currentThread()
						.getContextClassLoader()
						.getResource("rules/" + DEMO_GAME + "/greenBadges.drl")
						.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("greenBadges");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(Thread.currentThread()
						.getContextClassLoader()
						.getResource("rules/" + DEMO_GAME + "/greenPoints.drl")
						.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("greenPoints");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(
						Thread.currentThread()
								.getContextClassLoader()
								.getResource(
										"rules/" + DEMO_GAME
												+ "/healthPoints.drl")
								.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("healthPoints");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(
						Thread.currentThread()
								.getContextClassLoader()
								.getResource(
										"rules/" + DEMO_GAME
												+ "/healthBadges.drl")
								.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("healthBadges");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(Thread.currentThread()
						.getContextClassLoader()
						.getResource("rules/" + DEMO_GAME + "/prPoints.drl")
						.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("prPoints");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(Thread.currentThread()
						.getContextClassLoader()
						.getResource("rules/" + DEMO_GAME + "/prBadges.drl")
						.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("prBadges");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(Thread
						.currentThread()
						.getContextClassLoader()
						.getResource(
								"rules/" + DEMO_GAME + "/specialBadges.drl")
						.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("specialBadges");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(Thread
						.currentThread()
						.getContextClassLoader()
						.getResource(
								"rules/" + DEMO_GAME
										+ "/weekClassificationBadges.drl")
						.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("weekClassificationBadges");
				gameSrv.addRule(rule);

				c = FileUtils.readFileToString(new File(Thread
						.currentThread()
						.getContextClassLoader()
						.getResource(
								"rules/" + DEMO_GAME
										+ "/finalClassificationBadges.drl")
						.getFile()));
				rule = new DBRule(gameId, c);
				rule.setName("finalClassificationBadges");
				gameSrv.addRule(rule);

				logger.info("{} saved", gameName);
				return game;
			} catch (IOException e) {
				logger.error("Error loading {} rules", gameName);
				return null;
			}
		}
	}
}
