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
import eu.trentorise.game.core.config.TestCoreConfiguration;
import eu.trentorise.game.model.*;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.PlayerService;
import org.joda.time.DateTime;
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
public class HscGameTest {


    private static String GAME = "";
    private static final String BASEGAME = "HSC";
    private static final String ACTION = "save_itinerary";
    private static final String DOMAIN = "my-domain";

    private static final String[] TEAM1_PLAYERS = {"pippo", "topolino", "pluto"};

    private static final String[] TEAM2_PLAYERS = {"batman", "superman"};

    private static final String POINT_NAME =  "green leaves";
    private static final String POINT_NAME_TEAM =  "green leaves";

    private static final String PART_NAME =  "participation";

    private static final String WALK_KM =  "Walk_Km";

    private static final String BIKE_KM =  "Bike_Km";

    private static final String BUS_KM =  "Bus_Km";

    private static final String TRAIN_KM =  "Train_Km";

    private static final String FLAG_COLLECTION =  "flags";

    private static final String[] POINT_CONCEPTS = new String[]
            {POINT_NAME, PART_NAME, WALK_KM, BIKE_KM, BUS_KM, TRAIN_KM, "Walk_Trips", "Bike_Trips", "Bus_Trips", "Train_Trips", "BikeSharing_Km", "BikeSharing_Trips", "Carpooling_Km" , "Carpooling_Trips"};

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
    public void simpleScenario() throws Exception {
        prepare();
        doSomething();
    }

    public void prepare() throws Exception {
        // randomize game id
        GAME = BASEGAME + '_' + UUID.randomUUID();

        // define game
        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);
        game.setDomain(DOMAIN);

        HashSet<String> actions = new HashSet<>();
        actions.add(ACTION);
        game.setActions(actions);

        HashSet<GameConcept> gc = new HashSet<>();
        for (String s: POINT_CONCEPTS) {
            PointConcept pt = new PointConcept(s);
            pt.addPeriod("daily", new Date(), 60000);
            gc.add(pt);
        }
        gc.add(new BadgeCollectionConcept(FLAG_COLLECTION));
        game.setConcepts(gc);


        gameManager.saveGameDefinition(game);

        /*ClasspathRule rule = new ClasspathRule(GAME, "rules/" + BASEGAME + "/constants");
        rule.setName("constants");
        gameManager.addRule(rule);*/
        for (String s: new String[] {"constants", "itinery.drl",  "mode-counters.drl"}) {
            String url = "rules/" + BASEGAME + "/" + s;
            // check if url exists
            if (Thread.currentThread().getContextClassLoader().getResource(url) == null) {
                throw new Exception("rule file not found! - " + s);
            }
            ClasspathRule rule = new ClasspathRule(GAME, url);
            rule.setName(s);
            gameManager.addRule(rule);
        }

        // define player states

        print("add " + TEAM1_PLAYERS[0]);
        definePlayerState(TEAM1_PLAYERS[0]);
        print("add " + TEAM1_PLAYERS[1]);
        definePlayerState(TEAM1_PLAYERS[1]);
        print("add " + TEAM1_PLAYERS[2]);
        definePlayerState(TEAM1_PLAYERS[2]);
        print("add team");
        defineTeamState("disney", TEAM1_PLAYERS);


        definePlayerState(TEAM2_PLAYERS[0]);
        definePlayerState(TEAM2_PLAYERS[1]);
        defineTeamState("marvel", TEAM2_PLAYERS);

        print("done");
    }

    private void defineTeamState(String playerId, String[] players) {
        TeamState team = new TeamState(GAME, playerId);

        team.setMembers(List.of(players));
        team.setName(playerId);

        Set<GameConcept> myState = new HashSet<GameConcept>();
        PointConcept pc;
        pc = new PointConcept(POINT_NAME_TEAM);
        pc.setScore(0d);
        pc.addPeriod("daily", new Date(), 60000);

        myState.add(pc);

        team.setState(myState);

        System.out.println(team.getMembers());

        playerSrv.saveTeam(team);

        Map<String, Object> customData = new HashMap<>();
        customData.put("maxMembers", team.getMembers().size());
        playerSrv.updateCustomData(GAME, playerId, customData);

    }

    private void definePlayerState(String playerId) {

        PlayerState player = new PlayerState(GAME, playerId);
        Set<GameConcept> myState = new HashSet<>();
        for (String s: POINT_CONCEPTS) {
            PointConcept pc;
            pc = new PointConcept(s);
            pc.setScore(0d);
            pc.addPeriod("daily", new Date(), 60000);
            myState.add(pc);
        }

        player.setState(myState);

        playerSrv.saveState(player);
    }

    private void doSomething() throws Exception {

        // first player saves itinerary
        Map<String, Object> data = new HashMap<>();
        data.put("walkDistance", 6.0);
        data.put("travelId", "asdjsakdjsa9");

        PlayerState p = playerSrv.loadState(GAME, TEAM1_PLAYERS[0], false, false);
        p = engine.execute(GAME, p, ACTION, data, UUID.randomUUID().toString(),
                DateTime.now().getMillis(), null);
        p = playerSrv.saveState(p);

        print("ehilà");

        check(TEAM1_PLAYERS[0], POINT_NAME, 300.0 );

        p = playerSrv.loadState(GAME, TEAM1_PLAYERS[0], false, false);
        p = engine.execute(GAME, p, ACTION, data, UUID.randomUUID().toString(),
                DateTime.now().getMillis(), null);
        p = playerSrv.saveState(p);

        check(TEAM1_PLAYERS[0], POINT_NAME, 319.0 );

        // check(TEAM1_PLAYERS[0], PART_NAME, 40.0 * 20);
        check("disney", POINT_NAME, 1985.0);
    }
    
    private void check(String playerId, String conceptName, Double value) throws Exception {
        PlayerState state = playerSrv.loadState(GAME, playerId, false, false);
        double current = getScore(state, conceptName);
        Assert.assertTrue(current == value);
    }

    public double getScore(PlayerState ps, String name) throws Exception {
        for (GameConcept gc : ps.getState()) {
            if (gc instanceof PointConcept && gc.getName().equals(name)) {
                return ((PointConcept) gc).getScore();
            }
        }

       throw new Exception("GameConcept not found: " + name);
    }

    private void print(String text) {
        System.out.println("############ " + text);
    }

    /*
    private void doSomething() {

        PlayerState p = playerSrv.loadState(GAME, PLAYER, false, false);

        // give challenge
        LocalDate today = new LocalDate();

        Map<String, Object> chaData = new HashMap<>();
        chaData.put("target", 2);
        chaData.put("bonusScore", 100.0);
        chaData.put("bonusPointType", pointName);
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
//        t = new GeneralClassificationTask(null, 1, pointName, "week classification green");
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
//        check(new String[] {"silver-medal-green", "10-point-green"}, "1", pointName);
//        check(new String[] {"bronze-medal-pr"}, "1", "p+r");
//        check(new String[] {}, "1", "health");
//        check(new String[] {}, "1", "special");
//
//        // player 2
//        check(new String[] {"bronze-medal-green", "10-point-green"}, "2", pointName);
//        check(new String[] {"10-point-pr", "king-week-pr", "gold-medal-pr"}, "2", "p+r");
//        check(new String[] {"10-point-health", "25-point-health", "50-point-health",
//                "silver-medal-health"}, "2", "health");
//        check(new String[] {}, "2", "special");
//
//        // player 11
//        check(new String[] {"gold-medal-green", "10-point-green", "50-point-green",
//                "100-point-green", "king-week-green"}, "11", pointName);
//        check(new String[] {}, "11", "p+r");
//        check(new String[] {"10-point-health", "25-point-health", "50-point-health",
//                "king-week-health", "gold-medal-health"}, "11", "health");
//        check(new String[] {}, "11", "special");
//
//        // player 122
//        check(new String[] {}, "122", pointName);
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



    private Game defineGame() {



//        game.setTasks(new HashSet<GameTask>());
//
//        // final classifications
//        TaskSchedule schedule = new TaskSchedule();
//        schedule.setCronExpression("0 20 * * * *");
//        GeneralClassificationTask task1 = new GeneralClassificationTask(schedule, 3, pointName,
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
//        GeneralClassificationTask task4 = new GeneralClassificationTask(schedule, 1, pointName,
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

     */
    @Test
    public void testgetscore() {
        getScore("walk", 12);
    }

    double getScore(String mode, double distance) {
        double point = 20;
        // HSC special feature: multiply points by 16
        point *= 16;
        double limit = 1.5;
        double score = 0.0;
        int index = 0;
        while(index < 10) {
            score += Math.min(distance, limit) * point;
            System.out.printf("score: %.2f\n", score);
            distance -= limit;
            point /= 2;
            if (distance < 0) {
                break;
            }
            index++;
        }
        return score;
    }
}
