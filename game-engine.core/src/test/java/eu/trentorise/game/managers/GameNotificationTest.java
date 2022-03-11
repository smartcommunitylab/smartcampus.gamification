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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.config.TestCoreConfiguration;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.notification.GameNotification;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class,
		TestCoreConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class GameNotificationTest {

	private static final String GAME = "coreGameTest";
	private static final String ACTION = "save_itinerary";
	private static final String DOMAIN = "my-domain";
	private static final String PLAYER = "1";

	@Autowired
	private GameManager gameManager;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private GameWorkflow workflow;

	@Autowired
	private NotificationManager notificationManager;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void testGameNotification() throws InterruptedException {
		simpleEnv();
		execute();
		analyze();

	}

	public void simpleEnv() {
		// define game
		Game game = defineGame();
		gameManager.saveGameDefinition(game);
		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME + "/constants");
		rule.setName("constants");
		gameManager.addRule(rule);
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/healthBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/healthPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/prBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/prPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/specialBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/weekClassificationBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/finalClassificationBadges.drl"));
		// define player states
		mongo.save(definePlayerState("1", 15d, 0d, 0d));
	}

	private void execute() {
		Map<String, Object> data = new HashMap<>();
		data.put("travelId", 1);
		data.put("walkDistance", 0.25);
		data.put("p+r", true);
		workflow.apply(GAME, ACTION, PLAYER, data, null);
	}

	private void analyze() {
		List<Notification> notes = notificationManager.readNotifications(GAME);
		boolean found = false;
		GameNotification gameNotifica = null;
		for (Notification n : notes) {
			if (n.getType().equals(GameNotification.class.getSimpleName())) {
				found = true;
				gameNotifica = (GameNotification) n;
				break;
			}
		}
		Assert.assertEquals(true, found);
		Assert.assertEquals(25d, gameNotifica.getScore().doubleValue(), gameNotifica.getDelta().doubleValue());
	}

	private StatePersistence definePlayerState(String playerId, Double greenPoint, Double healthPoint, Double prPoint) {
		PlayerState player = new PlayerState(GAME, playerId);
		Set<GameConcept> myState = new HashSet<GameConcept>();
		PointConcept pc = new PointConcept("green leaves");
		pc.setScore(greenPoint);
		myState.add(pc);
		pc = new PointConcept("health");
		pc.setScore(healthPoint);
		myState.add(pc);
		pc = new PointConcept("p+r");
		pc.setScore(prPoint);
		myState.add(pc);
		BadgeCollectionConcept badge = new BadgeCollectionConcept("green leaves");
		myState.add(badge);
		badge = new BadgeCollectionConcept("health");
		myState.add(badge);
		badge = new BadgeCollectionConcept("p+r");
		myState.add(badge);
		badge = new BadgeCollectionConcept("special");
		myState.add(badge);
		player.setState(myState);

		return new StatePersistence(player);
	}

	private Game defineGame() {
		Game game = new Game();
		game.setId(GAME);
		game.setName(GAME);
		game.setDomain(DOMAIN);
		game.setNotifyPCName("green leaves");
		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(new PointConcept("green leaves"));
		game.getConcepts().add(new BadgeCollectionConcept("green leaves"));
		game.setTasks(new HashSet<GameTask>());

		return game;

	}

}
