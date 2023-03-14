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
import java.util.HashSet;

import org.joda.time.LocalDate;
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
import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.core.config.TestCoreConfiguration;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Level.Config;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
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

	@Autowired
	private GameManager gameManager;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private AppContextProvider provider;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void simpleEnv() {
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
		ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME + "/constants");
		rule.setName("constants");
		gameManager.addRule(rule);
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
		//action
		game.setActions(new HashSet<String>());
		game.getActions().add(ACTION);
		//point concepts
		game.setConcepts(new HashSet<GameConcept>());
		game.getConcepts().add(new PointConcept("PandR_Trips"));
		game.getConcepts().add(new PointConcept("Transit_Trips"));
		game.getConcepts().add(new PointConcept("BikeSharing_Km"));
		game.getConcepts().add(new PointConcept("Bike_Trips"));
		game.getConcepts().add(new PointConcept("Walk_Trips"));
		game.getConcepts().add(new PointConcept("Walk_Km"));
		game.getConcepts().add(new PointConcept("Car_Trips"));
		game.getConcepts().add(new PointConcept("Carpooling_Km"));
		game.getConcepts().add(new PointConcept("NoCar_Trips"));
		game.getConcepts().add(new PointConcept("Bus_Km"));
		game.getConcepts().add(new PointConcept("green leaves"));
		game.getConcepts().add(new PointConcept("Boat_Trips"));
		game.getConcepts().add(new PointConcept("BikeSharing_Trips"));
		game.getConcepts().add(new PointConcept("Car_Km"));
		game.getConcepts().add(new PointConcept("Train_Km"));
		game.getConcepts().add(new PointConcept("Recommendations"));
		game.getConcepts().add(new PointConcept("Bus_Trips"));
		game.getConcepts().add(new PointConcept("Train_Trips"));
		game.getConcepts().add(new PointConcept("Bike_Km"));
		game.getConcepts().add(new PointConcept("ZeroImpact_Trips"));
		//tasks
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
	
	private void upsertLevel(Game g) {
		Level level = new Level("Green Warrior", "green leaves");
		
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
		level.getThresholds().add(new Threshold("Green Influencer",	300d));
		level.getThresholds().add(new Threshold("Green Warrior", 700d));		
		level.getThresholds().add(new Threshold("Green Veteran", 800d));		
		level.getThresholds().add(new Threshold("Green Guru", 900d));		
		level.getThresholds().add(new Threshold("Green God", 1000d));			
		
		gameManager.upsertLevel(GAME, level);
		
	}
	
//	@Test
	private void scenario1() {
		
		
	}
}
