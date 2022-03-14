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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDateTime;
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
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Reward;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.notification.GameNotification;
import eu.trentorise.game.repo.ChallengeConceptPersistence;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GroupChallengeRepo;
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
	private static final String PLAYER2 = "2";

	@Autowired
	private GameManager gameManager;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private GameWorkflow workflow;

	@Autowired
	private NotificationManager notificationManager;

	@Autowired
	private GroupChallengeRepo groupChallengeConceptRepo;

	Date startDate = LocalDateTime.now().minusDays(5).toDate();

	Date endDate = LocalDateTime.now().plusDays(2).toDate();

	long ONE_DAY_MILLIS = 86400000;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
		mongo.dropCollection(GroupChallenge.class);
		mongo.dropCollection(ChallengeConceptPersistence.class);
	}

	@Test
	public void testGameNotification() throws InterruptedException {
		simpleEnv();
		execute();
		analyze();

	}

	@Test
	public void testGroupChallengeGameNotification() throws InterruptedException {
		simpleEnv();
		createGroupChallenge();
		executeGroupChallenge();
		analyzeGroupChallengeNotification();
	}

	private void analyzeGroupChallengeNotification() {
		List<Notification> notes = notificationManager.readNotifications(GAME);
		GameNotification gameNotifica = null;
		int countChallengeRewardNotification = 0;
		for (Notification n : notes) {
			if (n.getType().equals(GameNotification.class.getSimpleName())) {
				gameNotifica = (GameNotification) n;
				if (gameNotifica.getActionId().equals(GameManager.INTERNAL_ACTION_PREFIX + "reward")) {
					countChallengeRewardNotification++;	
				}
			}
		}
		Assert.assertEquals(2, countChallengeRewardNotification);
	}

	private void createGroupChallenge() {
		GroupChallenge groupChallenge = new GroupChallenge();
		groupChallenge.setGameId(GAME);
		Attendee proposer = new Attendee();
		proposer.setPlayerId(PLAYER);
		proposer.setRole(Role.PROPOSER);
		Attendee guest = new Attendee();
		guest.setPlayerId(PLAYER2);
		guest.setRole(Role.GUEST);
		groupChallenge.getAttendees().add(proposer);
		groupChallenge.getAttendees().add(guest);
		groupChallenge.setChallengeModel(GroupChallenge.MODEL_NAME_COOPERATIVE);
		groupChallenge.setChallengePointConcept(new PointConceptRef("NoCar_Trips", "weekly"));
		groupChallenge.setChallengeTarget(1);
		Reward reward = new Reward();
		reward.getBonusScore().put(PLAYER, 50.0);
		reward.getBonusScore().put(PLAYER2, 50.0);
		reward.setTargetPointConcept(new PointConceptRef("green leaves", "weekly"));
		reward.setCalculationPointConcept(new PointConceptRef("NoCar_Trips", "weekly"));
		groupChallenge.setReward(reward);
		groupChallenge.setStart(startDate);
		groupChallenge.setEnd(endDate);
		groupChallengeConceptRepo.save(groupChallenge);
	}

	private void executeGroupChallenge() {
		Map<String, Object> data = new HashMap<>();
		data.put("travelId", 1);
		data.put("walkDistance", 0.25);
		data.put("p+r", true);
		workflow.apply(GAME, ACTION, PLAYER, data, null);
	}

	public void simpleEnv() {
		// define game
		Game game = defineGame();
		gameManager.saveGameDefinition(game);
		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME + "/constants");
		rule.setName("constants");
		gameManager.addRule(rule);
		gameManager.addRule(new ClasspathRule(GAME, "rules/group-challenge/reward.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));
		// define player states
		mongo.save(definePlayerState("1", 15d, 0d, 0d));
		mongo.save(definePlayerState("2", 15d, 0d, 0d));
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
		PointConcept pc = new PointConcept("green leaves");
		pc.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
		game.getConcepts().add(pc);
		pc = new PointConcept("p+r");
		pc.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
		game.getConcepts().add(pc);
		pc = new PointConcept("NoCar_Trips");
		pc.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
		game.getConcepts().add(pc);
		game.getConcepts().add(new BadgeCollectionConcept("green leaves"));
		game.setTasks(new HashSet<GameTask>());

		return game;
	}

}
