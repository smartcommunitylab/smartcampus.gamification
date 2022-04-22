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

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.core.config.TestCoreConfiguration;
import eu.trentorise.game.model.*;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.GeneralClassificationTask;
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

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, TestCoreConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
public class VisitPointInterestTest {

    private static final String GAME = "coreGameTest";
    private static final String ACTION = "point_interest_reached";
    private static final String DOMAIN = "my-domain";
    private static final String PLAYER = "pippo";
    private static final String CHALLENGE = "visitPointInterest";
    private static final String TYPE_POI = "citta";

    @Autowired
    private GameManager gameManager;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private AppContextProvider provider;

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
    public void simpleScenario() throws InterruptedException {
        prepare();
        doSomething();
        check();

    }

    @Test
    public void prepare() {

        // define game
        Game game = defineGame();
        gameManager.saveGameDefinition(game);

        ClasspathRule rule = new ClasspathRule(GAME, "rules/" + GAME + "/constants");
        rule.setName("constants");
        gameManager.addRule(rule);
//        gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenBadges.drl"));
//        gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/greenPoints.drl"));
//        gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/healthBadges.drl"));
//        gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/healthPoints.drl"));
//        gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/prBadges.drl"));
//        gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/prPoints.drl"));
//        gameManager.addRule(new ClasspathRule(GAME, "rules/" + GAME + "/specialBadges.drl"));
//        gameManager.addRule(
//                new ClasspathRule(GAME, "rules/" + GAME + "/weekClassificationBadges.drl"));
//        gameManager.addRule(
//                new ClasspathRule(GAME, "rules/" + GAME + "/finalClassificationBadges.drl"));
        gameManager.addRule(
                new ClasspathRule(GAME, "rules/" + GAME + "/visitPoint.drl"));

        // define challenge
        ChallengeModel chaVisit = new ChallengeModel();
        chaVisit.setName(CHALLENGE);
        chaVisit.setGameId(GAME);
        chaVisit.setVariables(new HashSet<>(Arrays.asList("target", "bonusScore", "bonusPointType", "typePoi")));
        gameManager.saveChallengeModel(GAME, chaVisit);

        // define player states

        mongo.save(definePlayerState(PLAYER, 15d, 2d, 5d));

    }

    private void doSomething() {

        PlayerState p = playerSrv.loadState(GAME, PLAYER, false, false);

        // give challenge
        LocalDate today = new LocalDate();

        Map<String, Object> chaData = new HashMap<>();
        chaData.put("target", 2);
        chaData.put("bonusScore", 100);
        chaData.put("bonusPointType", "green leaves");
        chaData.put("typePoi", TYPE_POI);
        
        playerSrv.assignChallenge(GAME, PLAYER,
                new ChallengeAssignment(CHALLENGE, "challenge_easy", chaData, null,
                        today.dayOfMonth().addToCopy(-2).toDate(),
                        today.dayOfMonth().addToCopy(7).toDate()));

        chaData.put("target", 3);

        playerSrv.assignChallenge(GAME, PLAYER,
                new ChallengeAssignment(CHALLENGE, "challenge_medium", chaData, null,
                        today.dayOfMonth().addToCopy(-2).toDate(),
                        today.dayOfMonth().addToCopy(7).toDate()));

        // visited the first point of interest
        Map<String, Object> data = new HashMap<>();
        data.put("poi", "topolinia");
        data.put("typePoi", TYPE_POI);

        // first visit to topolinia
        p = engine.execute(GAME, p, ACTION, data, UUID.randomUUID().toString(),
                DateTime.now().getMillis(), null);

        Assert.assertTrue(hasBadge(p, TYPE_POI, "topolinia"));

        // second visit to topolinia
        p = engine.execute(GAME, p, ACTION, data, UUID.randomUUID().toString(),
                DateTime.now().getMillis(), null);

        Assert.assertTrue(hasBadge(p, TYPE_POI, "topolinia"));

        // first visit to paperopoli (2 cities)
        data.put("poi", "paperopoli");
        p = engine.execute(GAME, p, ACTION, data, UUID.randomUUID().toString(),
                DateTime.now().getMillis(), null);

        Assert.assertTrue(hasBadge(p, TYPE_POI, "paperopoli"));
        printBadges(p, TYPE_POI);
//
//        t = new GeneralClassificationTask(null, 1, "green leaves", "week classification green");
//        t.execute((GameContext) provider.getApplicationContext().getBean("gameCtx", GAME, t));
//
//        t = new GeneralClassificationTask(null, 1, "health", "week classification health");
//        t.execute((GameContext) provider.getApplicationContext().getBean("gameCtx", GAME, t));
//
//        t = new GeneralClassificationTask(null, 1, "p+r", "week classification p+r");
//        t.execute((GameContext) provider.getApplicationContext().getBean("gameCtx", GAME, t));
//
//        // final classification
//
//        t = new GeneralClassificationTask(null, 3, "health", "final classification health");
//        t.execute((GameContext) provider.getApplicationContext().getBean("gameCtx", GAME, t));
//
//        t = new GeneralClassificationTask(null, 3, "p+r", "final classification p+r");
//        t.execute((GameContext) provider.getApplicationContext().getBean("gameCtx", GAME, t));
    }

    public void check() {

//        // player 1
//        check(new String[] {"silver-medal-green", "10-point-green"}, "1", "green leaves");
//        check(new String[] {"bronze-medal-pr"}, "1", "p+r");
//        check(new String[] {}, "1", "health");
//        check(new String[] {}, "1", "special");
//
//        // player 2
//        check(new String[] {"bronze-medal-green", "10-point-green"}, "2", "green leaves");
//        check(new String[] {"10-point-pr", "king-week-pr", "gold-medal-pr"}, "2", "p+r");
//        check(new String[] {"10-point-health", "25-point-health", "50-point-health",
//                "silver-medal-health"}, "2", "health");
//        check(new String[] {}, "2", "special");
//
//        // player 11
//        check(new String[] {"gold-medal-green", "10-point-green", "50-point-green",
//                "100-point-green", "king-week-green"}, "11", "green leaves");
//        check(new String[] {}, "11", "p+r");
//        check(new String[] {"10-point-health", "25-point-health", "50-point-health",
//                "king-week-health", "gold-medal-health"}, "11", "health");
//        check(new String[] {}, "11", "special");
//
//        // player 122
//        check(new String[] {}, "122", "green leaves");
//        check(new String[] {"10-point-pr", "silver-medal-pr"}, "122", "p+r");
//        check(new String[] {"bronze-medal-health", "10-point-health"}, "122", "health");
//        check(new String[] {}, "122", "special");

    }

//    private void check(String[] values, String playerId, String conceptName) {
//        List<PlayerState> states = playerSrv.loadStates(GAME);
//        StateAnalyzer analyzer = new StateAnalyzer(states);
//        Assert.assertTrue(String.format("Failure concept %s of  player %s", conceptName, playerId),
//                new HashSet<String>(Arrays.asList(values)).containsAll(
//                        analyzer.getBadges(analyzer.findPlayer(playerId), conceptName)));
//    }

    public boolean hasBadge(PlayerState ps, String collection, String badge) {
        printBadges(ps, collection);

        for (String s : getBadges(ps, collection)) {
            if (s.equals(badge)) return true;
        }

        System.out.println(" ");

        return false;
    }

    private void printBadges(PlayerState ps, String collection) {
        System.out.printf("collection %s of player %s:", collection, ps.getPlayerId());
        for (String s : getBadges(ps, collection))
            System.out.print(s + ' ');
    }


    public List<String> getBadges(PlayerState ps, String name) {
        for (GameConcept gc : ps.getState()) {
            if (gc instanceof BadgeCollectionConcept && gc.getName().equals(name)) {
                return ((BadgeCollectionConcept) gc).getBadgeEarned();
            }
        }

        return Collections.<String>emptyList();
    }

    class StateAnalyzer {
        private List<PlayerState> s;

        public StateAnalyzer(List<PlayerState> s) {
            this.s = s;

        }

        public double getScore(PlayerState ps, String name) {
            for (GameConcept gc : ps.getState()) {
                if (gc instanceof PointConcept && gc.getName().equals(name)) {
                    return ((PointConcept) gc).getScore();
                }
            }

            return 0d;
        }

        public List<String> getBadges(PlayerState ps, String name) {
            for (GameConcept gc : ps.getState()) {
                if (gc instanceof BadgeCollectionConcept && gc.getName().equals(name)) {
                    return ((BadgeCollectionConcept) gc).getBadgeEarned();
                }
            }

            return Collections.<String>emptyList();
        }

        public PlayerState findPlayer(String playerId) {
            for (PlayerState ps : s) {
                if (ps.getPlayerId().equals(playerId)) {
                    return ps;
                }
            }

            return new PlayerState(definePlayerState(playerId, 0d, 0d, 0d));
        }
    }

    private StatePersistence definePlayerState(String playerId, Double greenPoint,
            Double healthPoint, Double prPoint) {
        PlayerState player = new PlayerState(GAME, playerId);
        Set<GameConcept> myState = new HashSet<GameConcept>();
        PointConcept pc;
        pc = new PointConcept("green leaves");
        pc.setScore(greenPoint);
        myState.add(pc);

        BadgeCollectionConcept badge;
        badge = new BadgeCollectionConcept("green leaves");
        myState.add(badge);

        badge = new BadgeCollectionConcept(TYPE_POI);
        myState.add(badge);

        player.setState(myState);

        return new StatePersistence(player);
    }

    private Game defineGame() {
        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);
        game.setDomain(DOMAIN);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);

        game.setConcepts(new HashSet<GameConcept>());
        game.getConcepts().add(new PointConcept("green leaves"));

        game.getConcepts().add(new BadgeCollectionConcept("green leaves"));

        game.getConcepts().add(new BadgeCollectionConcept(TYPE_POI));

//        game.setTasks(new HashSet<GameTask>());
//
//        // final classifications
//        TaskSchedule schedule = new TaskSchedule();
//        schedule.setCronExpression("0 20 * * * *");
//        GeneralClassificationTask task1 = new GeneralClassificationTask(schedule, 3, "green leaves",
//                "final classification green");
//        game.getTasks().add(task1);
//
//        // schedule = new TaskSchedule(); //
//        schedule.setCronExpression("0 * * * * *");
//        GeneralClassificationTask task2 =
//                new GeneralClassificationTask(schedule, 3, "health", "final classification health");
//        game.getTasks().add(task2);
//
//        // schedule = new TaskSchedule(); //
//        schedule.setCronExpression("0 * * * * *");
//        GeneralClassificationTask task3 =
//                new GeneralClassificationTask(schedule, 3, "p+r", "final classification p+r");
//        game.getTasks().add(task3);
//
//        // week classifications // schedule = new TaskSchedule(); //
//        schedule.setCronExpression("0 * * * * *");
//        GeneralClassificationTask task4 = new GeneralClassificationTask(schedule, 1, "green leaves",
//                "week classification green");
//        game.getTasks().add(task4);
//
//        // schedule = new TaskSchedule(); //
//        schedule.setCronExpression("0 * * * * *");
//        GeneralClassificationTask task5 =
//                new GeneralClassificationTask(schedule, 1, "health", "week classification health");
//        game.getTasks().add(task5);
//
//        // schedule = new TaskSchedule(); //
//        schedule.setCronExpression("0 * * * * *");
//        GeneralClassificationTask task6 =
//                new GeneralClassificationTask(schedule, 1, "p+r", "week classification p+r");
//        game.getTasks().add(task6);

        return game;

    }
}
