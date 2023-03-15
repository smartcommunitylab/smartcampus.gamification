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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.core.config.TestCoreConfiguration;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Config;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.GeneralClassificationTask;
import eu.trentorise.game.task.IncrementalClassificationTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class,
		TestCoreConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class CityTest {

	private static final String GAME = "city-test";
	private static final String ACTION = "save_itinerary";
	private static final String DOMAIN = "my-domain";
	private static final String LEVEL_NAME = "Green Warrior";
	private static final String POINT_NAME = "green leaves";
	private static final String[] POINT_CONCEPTS = new String[] { "PandR_Trips", "Transit_Trips", "BikeSharing_Km",
			"Bike_Trips", "Walk_Trips", "Walk_Km", "Car_Trips", "Carpooling_Km", "NoCar_Trips", "Bus_Km", POINT_NAME,
			"Boat_Trips", "Car_Km", "BikeSharing_Trips", "Train_Km", "Recommendations", "Bus_Trips", "Train_Trips",
			"Bike_Km", "Carpooling_Trips" };
	private static final String[] BADGES_COLLECTION = new String[] { "park and ride pioneer",
			"public transport aficionado", "recommendations", "bike sharing pioneer", "green leaves", "festa",
			"sustainable life", "leaderboard top 3", "bike aficionado" };

	@Autowired
	private GameManager gameManager;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private GameEngine engine;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void levelTest() throws Exception {
		init();
		scenario1();
	}

	private void init() throws Exception {
		// define game
		Game game = defineGame();
		gameManager.saveGameDefinition(game);
		// add level.
		upsertLevel(game);
		// rules.
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_absoluteIncrement.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_boat.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_checkin.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_incentiveGroupChallengeReward.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_multiTarget.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_nextBadge.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_percentageIncrement.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/challenge_repetitiveBehaviour.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/constants.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/finalClassificationBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/mode-counters.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/poiPoints.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/specialBadges.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/survey.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/visitPointInterest.drl"));
		gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/weekClassificationBadges.drl"));

	}

	private Game defineGame() {
		Game game = new Game();
		game.setId(GAME);
		game.setName(GAME);
		game.setDomain(DOMAIN);
		// action
		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		// point concepts
		HashSet<GameConcept> gc = new HashSet<>();
		for (String s : POINT_CONCEPTS) {
			PointConcept pt = new PointConcept(s);
			pt.addPeriod("daily", new Date(), 60000);
			gc.add(pt);
		}
		game.setConcepts(gc);
		// badges
		for (String badge : BADGES_COLLECTION) {
			gc.add(new BadgeCollectionConcept(badge));
		}
		// tasks
		game.setTasks(new HashSet<GameTask>());
		PointConcept score = new PointConcept("green leaves");
		Date today = LocalDate.now().toDate();
		long oneDay = 86400000;
		score.addPeriod("my-period", today, oneDay);
		IncrementalClassificationTask task1 = new IncrementalClassificationTask(score, "my-period",
				"week classification green");
		game.getTasks().add(task1);
		TaskSchedule schedule = new TaskSchedule();
		schedule = new TaskSchedule(); //
		schedule.setCronExpression("0 0 1 1 1 2030 *");
		GeneralClassificationTask task2 = new GeneralClassificationTask(schedule, 3, "green leaves",
				"global classification green");
		game.getTasks().add(task2);

		return game;

	}

	private void upsertLevel(Game g) throws Exception {
		Level level = new Level(LEVEL_NAME, POINT_NAME);

		Threshold groupCooperative = new Threshold("Green Soldier", 400d);
		Config groupCooperativeLevelConfig = new Config();
		groupCooperativeLevelConfig.getAvailableModels().add("groupCooperative");
		groupCooperativeLevelConfig.setChoices(0);
		groupCooperative.setConfig(groupCooperativeLevelConfig);
		level.getThresholds().add(groupCooperative);

		Threshold groupCompetitiveTime = new Threshold("Green Master", 500d);
		Config groupCompetitiveTimeLevelConfig = new Config();
		groupCompetitiveTimeLevelConfig.getAvailableModels().add("groupCompetitiveTime");
		groupCompetitiveTimeLevelConfig.setChoices(0);
		groupCompetitiveTime.setConfig(groupCompetitiveTimeLevelConfig);
		level.getThresholds().add(groupCompetitiveTime);

		Threshold groupCompetitivePerformance = new Threshold("Green Ambassador", 600d);
		Config groupCompetitivePerformanceLevelConfig = new Config();
		groupCompetitivePerformanceLevelConfig.getAvailableModels().add("groupCompetitivePerformance");
		groupCompetitivePerformanceLevelConfig.setChoices(0);
		groupCompetitivePerformance.setConfig(groupCompetitivePerformanceLevelConfig);
		level.getThresholds().add(groupCompetitivePerformance);

		level.getThresholds().add(new Threshold("Green Starter", 0d));
		level.getThresholds().add(new Threshold("Green Follower", 100d));
		level.getThresholds().add(new Threshold("Green Lover", 200d));
		level.getThresholds().add(new Threshold("Green Influencer", 300d));
		level.getThresholds().add(new Threshold("Green Warrior", 700d));
		level.getThresholds().add(new Threshold("Green Veteran", 800d));
		level.getThresholds().add(new Threshold("Green Guru", 900d));
		level.getThresholds().add(new Threshold("Green God", 1000d));

		gameManager.upsertLevel(GAME, level);

	}

	public void scenario1() throws Exception {

		/**
		 * SCENARIO 1 **
		 * https://docs.google.com/document/d/1njKGeraPiM5ljlhwwOZUn8vVvvjTZDxJy1IC2TdV5sU/edit#
		 * 
		 * Assign points step by step (100, 200, .... 100) and check if levels reached
		 * 
		 * playerId			 Level				 Points
		 *  ---------------------------------------------- 
		 * A				"Green Starter" 		100
		 * B 				"Green Follower" 		200
		 * C 				"Green Influencer" 		300
		 * D 				"Green Soldier" 		400
		 * E 				"Green Master" 			500
		 * F 				"Green Ambassador"		600
		 * G 				"Green Warrior"			700
		 * H 				"Green Veteran"			800
		 * I 				"Green Guru"			900
		 * J 				"Green God" 			1000
		 */

		// A "Green Starter" 100
		definePlayerState("A");
		Map<String, Object> dataA = new HashMap<>();
		dataA.put("walkDistance", 50.0);
		dataA.put("trackId", "A");
		PlayerState psA = playerSrv.loadState(GAME, "A", true, false);
		psA = engine.execute(GAME, psA, ACTION, dataA, UUID.randomUUID().toString(), DateTime.now().getMillis(), null);
		psA = playerSrv.saveState(psA);
		printScore(psA);
		Assert.assertTrue(psA.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Follower"));

		// B "Green Follower" 200
		definePlayerState("B");
		Map<String, Object> dataB = new HashMap<>();
		dataB.put("bikeDistance", 40.0);
		dataB.put("trackId", "B1");
		PlayerState psB = playerSrv.loadState(GAME, "B", true, false);
		psB = engine.execute(GAME, psB, ACTION, dataB, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psB = playerSrv.saveState(psB);
		printScore(psB);
		Assert.assertTrue(psB.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Lover"));

		//	C "Green Influencer" 300
		definePlayerState("C");
		Map<String, Object> dataC = new HashMap<>();
		dataC.put("bikeDistance", 50.0);
		dataC.put("trackId", "C1");
		PlayerState psC = playerSrv.loadState(GAME, "C", true, false);
		psC = engine.execute(GAME, psC, ACTION, dataC, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		printScore(psC);
		System.out.println(psC.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psC.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Influencer"));
		
		// D "Green Soldier" 400
		definePlayerState("D");
		Map<String, Object> dataD = new HashMap<>();
		dataD.put("bikeDistance", 70.0);
		dataD.put("trackId", "D1");
		PlayerState psD = playerSrv.loadState(GAME, "D", true, false);
		psD = engine.execute(GAME, psD, ACTION, dataD, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psD = playerSrv.saveState(psD);
		printScore(psD);
		System.out.println(psD.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psD.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Soldier"));
		
		// E "Green Master" 500
		definePlayerState("E");
		Map<String, Object> dataE = new HashMap<>();
		dataE.put("bikeDistance", 90.0);
		dataE.put("trackId", "e1");
		PlayerState psE = playerSrv.loadState(GAME, "E", true, false);
		psE = engine.execute(GAME, psE, ACTION, dataE, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psE = playerSrv.saveState(psE);
		printScore(psE);
		System.out.println(psE.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psE.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Master"));
		
		// F "Green Ambassador"	600
		definePlayerState("F");
		Map<String, Object> dataF = new HashMap<>();
		dataF.put("bikeDistance", 110.0);
		dataF.put("trackId", "f1");
		PlayerState psF = playerSrv.loadState(GAME, "F", true, false);
		psF = engine.execute(GAME, psF, ACTION, dataF, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psF = playerSrv.saveState(psF);
		printScore(psF);
		System.out.println(psF.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psF.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Ambassador"));
		
//		 * G "Green Warrior" 700
		definePlayerState("G");
		Map<String, Object> dataG = new HashMap<>();
		dataG.put("bikeDistance", 130.0);
		dataG.put("trackId", "G1");
		PlayerState psG = playerSrv.loadState(GAME, "G", true, false);
		psG = engine.execute(GAME, psG, ACTION, dataG, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psG = playerSrv.saveState(psG);
		printScore(psG);
		System.out.println(psG.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psG.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Warrior"));

//		H "Green Veteran" 800
		definePlayerState("H");
		Map<String, Object> dataH = new HashMap<>();
		dataH.put("bikeDistance", 140.0);
		dataH.put("trackId", "H1");
		PlayerState psH = playerSrv.loadState(GAME, "H", true, false);
		psH = engine.execute(GAME, psH, ACTION, dataH, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psH = playerSrv.saveState(psH);
		printScore(psH);
		System.out.println(psH.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psH.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Veteran"));


//		I "Green Guru" 900
		definePlayerState("I");
		Map<String, Object> dataI = new HashMap<>();
		dataI.put("bikeDistance", 150.0);
		dataI.put("trackId", "I1");
		PlayerState psI = playerSrv.loadState(GAME, "I", true, false);
		psI = engine.execute(GAME, psI, ACTION, dataI, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psI = playerSrv.saveState(psI);
		printScore(psI);
		System.out.println(psI.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psI.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green Guru"));

//		J "Green God" 1000
		definePlayerState("J");
		Map<String, Object> dataJ = new HashMap<>();
		dataJ.put("bikeDistance", 170.0);
		dataJ.put("trackId", "J1");
		PlayerState psJ = playerSrv.loadState(GAME, "J", true, false);
		psJ = engine.execute(GAME, psJ, ACTION, dataJ, UUID.randomUUID().toString(), DateTime.now().minusDays(2).getMillis(), null);
		psJ = playerSrv.saveState(psJ);
		printScore(psJ);
		System.out.println(psJ.getLevels().get(0).getLevelValue());
		Assert.assertTrue(psJ.getLevels().get(0).getLevelValue().equalsIgnoreCase("Green God"));
		
		// SCENARIO 2 <inside ChallengeGenerator>

	}

	private void definePlayerState(String playerId) {
		PlayerState player = new PlayerState(GAME, playerId);
		Set<GameConcept> myState = new HashSet<>();
		PointConcept pc = new PointConcept(POINT_NAME);
		pc.setScore(0d);
		pc.addPeriod("daily", new Date(), 60000);
		myState.add(pc);
		player.setState(myState);
		playerSrv.saveState(player);
	}

	private void printScore(PlayerState p) {
		for (GameConcept gc : p.getState()) {
			if (gc instanceof PointConcept && gc.getName().equals(POINT_NAME)) {
				System.out.println(((PointConcept) gc).getScore().doubleValue());
				break;
			}
		}
	}

}
