package eu.trentorise.game.managers;


import static eu.trentorise.game.test_utils.Utils.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.Clock;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class GameManagerTest {

    @Autowired
    private GameService gameSrv;

    @Autowired
    @InjectMocks
    private ChallengeManager challengeManager;

    @Autowired
    private MongoTemplate mongo;

    @Mock
    private Clock clock;

    @Autowired
    private PlayerService playerSrv;

    @Before
    public void setup() {
        mongo.getDb().dropDatabase();
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = IllegalArgumentException.class)
    public void define_a_level_for_a_not_existent_game() {
        Level level = new Level("miner", "green leaves");
        String gameId = "MY_GAME";
        @SuppressWarnings("unused")
        Game game = gameSrv.upsertLevel(gameId, level);
    }

    @Test
    public void define_first_level() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green leaves"));
        gameSrv.saveGameDefinition(g);
        Level level = new Level("miner", "green leaves");
        String gameId = "MY_GAME";
        Game game = gameSrv.upsertLevel(gameId, level);
        Assert.assertEquals(1, game.getLevels().size());

    }

    @Test
    public void add_new_level_to_game() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green leaves"));
        g.getConcepts().add(new PointConcept("BikeKm"));
        g.getLevels().add(new Level("miner", "green leaves"));
        gameSrv.saveGameDefinition(g);

        Level level = new Level("explorer", "BikeKm");
        String gameId = "MY_GAME";
        Game game = gameSrv.upsertLevel(gameId, level);
        Assert.assertEquals(2, game.getLevels().size());
    }

    @Test
    public void edit_a_level() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green leaves"));
        g.getConcepts().add(new PointConcept("BikeKm"));
        Level level = new Level("miner", "green leaves");
        g.getLevels().add(level);
        gameSrv.saveGameDefinition(g);
        
        level.getThresholds().add(new Threshold("first", 50d));
        level.getThresholds().add(new Threshold("second", 100d));

        Game game = gameSrv.upsertLevel("MY_GAME", level);
        Assert.assertEquals(1, game.getLevels().size());
        Assert.assertEquals(2, game.getLevels().get(0).getThresholds().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannot_add_multiple_level_on_same_pointConcept() {

        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("BikeKm"));
        g.getLevels().add(new Level("hero", "BikeKm"));
        gameSrv.saveGameDefinition(g);

        Level level = new Level("explorer", "BikeKm");
        String gameId = "MY_GAME";
        @SuppressWarnings("unused")
        Game game = gameSrv.upsertLevel(gameId, level);
    }

    @Test(expected = IllegalArgumentException.class)
    public void level_name_cannot_be_blank() {
        @SuppressWarnings("unused")
        Level level = new Level(" ", "green leaves");
    }

    @Test(expected = IllegalArgumentException.class)
    public void pointConcept_cannot_be_blank() {
        @SuppressWarnings("unused")
        Level level = new Level("miner", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void level_should_refer_to_valid_pointConcept() {
        Game g = new Game("MY_GAME");
        gameSrv.saveGameDefinition(g);
        Level level = new Level("miner", "DUMMIE SCORE");
        String gameId = "MY_GAME";
        @SuppressWarnings("unused")
        Game game = gameSrv.upsertLevel(gameId, level);
    }


    @Test
    public void delete_existing_level() {
        Game g = new Game("MY_GAME");
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        String levelName = "miner";
        String gameId = "MY_GAME";
        Game game = gameSrv.deleteLevel(gameId, levelName);
        Assert.assertEquals(0, game.getLevels().size());
    }

    @Test
    public void delete_not_existing_level() {
        Game g = new Game("MY_GAME");
        g.getLevels().add(new Level("investigator", "yellow"));
        gameSrv.saveGameDefinition(g);
        String levelName = "miner";
        String gameId = "MY_GAME";
        Game game = gameSrv.deleteLevel(gameId, levelName);
        Assert.assertEquals(1, game.getLevels().size());
    }


    @Test
    public void add_threshold_to_level_with_zero_thresholds() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        String gameId = "MY_GAME";
        String levelName = "miner";
        String name = "Beginner";
        double thresholdValue = 200d;
        Level lev =
                gameSrv.addLevelThreshold(gameId, levelName, new Threshold(name, thresholdValue));
        Assert.assertEquals(1, lev.getThresholds().size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void add_already_existent_threshold() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        String gameId = "MY_GAME";
        String levelName = "miner";
        String name = "Beginner";
        double thresholdValue = 200d;
        gameSrv.addLevelThreshold(gameId, levelName, new Threshold(name, thresholdValue));

        gameSrv.addLevelThreshold(gameId, levelName, new Threshold(name, 400d));
    }



    @Test
    public void add_second_threshold() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        String gameId = "MY_GAME";
        String levelName = "miner";
        String name = "Beginner";
        double thresholdValue = 200d;
        gameSrv.addLevelThreshold(gameId, levelName, new Threshold(name, thresholdValue));

        Level lev = gameSrv.addLevelThreshold(gameId, levelName, new Threshold("Expert", 1000d));
        Assert.assertEquals(2, lev.getThresholds().size());
    }


    @Test
    public void delete_a_threshold() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        String gameId = "MY_GAME";
        String levelName = "miner";
        String name = "Beginner";
        double thresholdValue = 200d;
        Level lev =
                gameSrv.addLevelThreshold(gameId, levelName, new Threshold(name, thresholdValue));
        Assert.assertEquals(1, lev.getThresholds().size());

        lev = gameSrv.deleteLevelThreshold(gameId, levelName, "Beginner");
        Assert.assertEquals(0, lev.getThresholds().size());
    }

    @Test
    public void delete_not_existent_threshold() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        String gameId = "MY_GAME";
        String levelName = "miner";
        String name = "Beginner";
        double thresholdValue = 200d;
        gameSrv.addLevelThreshold(gameId, levelName, new Threshold(name, thresholdValue));

        Level lev = gameSrv.deleteLevelThreshold(gameId, levelName, "Expert");
        Assert.assertEquals(1, lev.getThresholds().size());
    }

    @Test
    public void edit_threshold_value() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        gameSrv.addLevelThreshold("MY_GAME", "miner", new Threshold("beginner", 100d));
        Level lev = gameSrv.updateLevelThreshold("MY_GAME", "miner", "beginner", 400d);
        Assert.assertEquals(400d, lev.getThresholds().get(0).getValue(), 0);
    }

    @Test
    public void edit_not_existent_threshold() {
        Game g = new Game("MY_GAME");
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getLevels().add(new Level("miner", "green"));
        gameSrv.saveGameDefinition(g);
        gameSrv.addLevelThreshold("MY_GAME", "miner", new Threshold("beginner", 100d));
        Level lev = gameSrv.updateLevelThreshold("MY_GAME", "miner", "expert", 400d);
        Assert.assertEquals(100d, lev.getThresholds().get(0).getValue(), 0);
    }

    @Test
    public void return_actual_level() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));

        Level explorerLevel = new Level("explorer", "green");
        explorerLevel.getThresholds().add(new Threshold("child", 0d));
        explorerLevel.getThresholds().add(new Threshold("adept", 100d));
        g.getLevels().add(explorerLevel);

        g = gameSrv.saveGameDefinition(g);


        PlayerState playerState = new PlayerState(gameId, "player");
        PointConcept greenScore = new PointConcept("green");
        greenScore.setScore(56d);
        playerState.getState().add(greenScore);

        List<PlayerLevel> levels = gameSrv.calculateLevels(gameId, playerState);

        Assert.assertEquals(1, levels.size());
        Assert.assertEquals("child", levels.get(0).getLevelValue());
        Assert.assertEquals(0, levels.get(0).getLevelIndex());
        Assert.assertEquals(44d, levels.get(0).getToNextLevel(), 0);
    }

    @Test
    public void gain_level_on_threshold() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));

        Level explorerLevel = new Level("explorer", "green");
        explorerLevel.getThresholds().add(new Threshold("child", 0d));
        explorerLevel.getThresholds().add(new Threshold("adept", 100d));
        g.getLevels().add(explorerLevel);

        g = gameSrv.saveGameDefinition(g);


        PlayerState playerState = new PlayerState(gameId, "player");
        PointConcept greenScore = new PointConcept("green");
        greenScore.setScore(100d);
        playerState.getState().add(greenScore);

        List<PlayerLevel> levels = gameSrv.calculateLevels(gameId, playerState);

        Assert.assertEquals(1, levels.size());
        Assert.assertEquals("adept", levels.get(0).getLevelValue());
        Assert.assertEquals(1, levels.get(0).getLevelIndex());
        Assert.assertEquals(0d, levels.get(0).getToNextLevel(), 0);
    }

    @Test
    public void state_with_score_at_zero() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));

        Level explorerLevel = new Level("explorer", "green");
        explorerLevel.getThresholds().add(new Threshold("child", 0d));
        explorerLevel.getThresholds().add(new Threshold("adept", 100d));
        g.getLevels().add(explorerLevel);

        g = gameSrv.saveGameDefinition(g);


        PlayerState playerState = new PlayerState(gameId, "player");
        PointConcept greenScore = new PointConcept("green");
        greenScore.setScore(0d);
        playerState.getState().add(greenScore);

        List<PlayerLevel> levels = gameSrv.calculateLevels(gameId, playerState);
        Assert.assertEquals(1, levels.size());
        Assert.assertEquals("child", levels.get(0).getLevelValue());
        Assert.assertEquals(100d, levels.get(0).getToNextLevel(), 0);
    }

    @Test
    public void return_multiple_levels() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getConcepts().add(new PointConcept("black"));

        Level explorerLevel = new Level("explorer", "green");
        explorerLevel.getThresholds().add(new Threshold("child", 0d));
        explorerLevel.getThresholds().add(new Threshold("adept", 100d));
        g.getLevels().add(explorerLevel);

        explorerLevel = new Level("warrior", "black");
        explorerLevel.getThresholds().add(new Threshold("foot soldier", 0d));
        explorerLevel.getThresholds().add(new Threshold("assassin", 500d));
        g.getLevels().add(explorerLevel);

        g = gameSrv.saveGameDefinition(g);
        
        PlayerState playerState = new PlayerState(gameId, "player");
        PointConcept greenScore = new PointConcept("green");
        greenScore.setScore(56d);
        playerState.getState().add(greenScore);

        PointConcept blackScore = new PointConcept("black");
        blackScore.setScore(0d);
        playerState.getState().add(blackScore);

        List<PlayerLevel> levels = gameSrv.calculateLevels(gameId, playerState);
        Assert.assertEquals(2, levels.size());
        Assert.assertEquals("child", levels.get(0).getLevelValue());
        Assert.assertEquals(0, levels.get(0).getLevelIndex());
        Assert.assertEquals(44d, levels.get(0).getToNextLevel(), 0);
        Assert.assertEquals("foot soldier", levels.get(1).getLevelValue());
        Assert.assertEquals(0, levels.get(1).getLevelIndex());
        Assert.assertEquals(500d, levels.get(1).getToNextLevel(), 0);
    }

    @Test
    public void level_when_score_in_state_is_missing() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g.getConcepts().add(new PointConcept("black"));

        Level explorerLevel = new Level("explorer", "green");
        explorerLevel.getThresholds().add(new Threshold("child", 0d));
        explorerLevel.getThresholds().add(new Threshold("adept", 100d));
        g.getLevels().add(explorerLevel);

        g = gameSrv.saveGameDefinition(g);

        PlayerState playerState = new PlayerState(gameId, "player");

        List<PlayerLevel> levels = gameSrv.calculateLevels(gameId, playerState);
        Assert.assertEquals(1, levels.size());
        Assert.assertEquals("child", levels.get(0).getLevelValue());
        Assert.assertEquals(0, levels.get(0).getLevelIndex());
        Assert.assertEquals(100d, levels.get(0).getToNextLevel(), 0);
    }

    @Test
    public void return_no_next_level() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));

        Level explorerLevel = new Level("explorer", "green");
        explorerLevel.getThresholds().add(new Threshold("child", 0d));
        explorerLevel.getThresholds().add(new Threshold("adept", 100d));
        g.getLevels().add(explorerLevel);

        g = gameSrv.saveGameDefinition(g);

        PlayerState playerState = new PlayerState(gameId, "player");
        PointConcept greenScore = new PointConcept("green");
        greenScore.setScore(200d);
        playerState.getState().add(greenScore);

        List<PlayerLevel> levels = gameSrv.calculateLevels(gameId, playerState);
        Assert.assertEquals(1, levels.size());
        Assert.assertEquals("adept", levels.get(0).getLevelValue());
        Assert.assertEquals(1, levels.get(0).getLevelIndex());
        Assert.assertEquals(0d, levels.get(0).getToNextLevel(), 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void player_level_on_level_without_thresholds() {
        Level levelDefinition = new Level("greener", "green leaves");
        PlayerLevel level = new PlayerLevel(levelDefinition, 10);
    }

    @Test(expected = NoSuchElementException.class)
    public void player_level_on_player_with_score_under_minimum_threshold() {
        Level levelDefinition = new Level("greener", "green leaves");
        levelDefinition.getThresholds().add(new Threshold("0", 500));
        PlayerLevel level = new PlayerLevel(levelDefinition, 10);
    }

    @Test
    public void player_level_on_player_with_score_equals_to_minimum_threshold() {
        Level levelDefinition = new Level("greener", "green leaves");
        levelDefinition.getThresholds().add(new Threshold("0", 0));
        PlayerLevel level = new PlayerLevel(levelDefinition, 0);
        assertThat(level.getLevelValue(), is("0"));
    }

    @Test
    public void thresholds_should_be_sorted_by_value() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));

        Level explorerLevel = new Level("explorer", "green");
        explorerLevel.getThresholds().add(new Threshold("adept", 100d));
        explorerLevel.getThresholds().add(new Threshold("child", 0d));
        explorerLevel.getThresholds().add(new Threshold("master", 400d));

        List<Threshold> thresholds = explorerLevel.getThresholds();

        assertThat(thresholds, contains(new Threshold("child", 0d), new Threshold("adept", 100d),
                new Threshold("master", 400d)));


    }

    @Test
    public void no_level_definitions_for_game() {
        String gameId = "MY_GAME";

        Game g = new Game(gameId);
        g.setConcepts(new HashSet<>());
        g.getConcepts().add(new PointConcept("green"));
        g = gameSrv.saveGameDefinition(g);

        PlayerState playerState = new PlayerState(gameId, "player");
        PointConcept greenScore = new PointConcept("green");
        greenScore.setScore(200d);
        playerState.getState().add(greenScore);

        List<PlayerLevel> levels = gameSrv.calculateLevels(gameId, playerState);
        Assert.assertEquals(0, levels.size());
    }

    @Test
    public void competitive_performance_completion_check_process() {
        // save an active game
        Game g = new Game("game");
        gameSrv.saveGameDefinition(g);

        Date startDate = date("2018-09-26T00:00:00");
        long executionMoment = new DateTime(startDate.getTime()).plusHours(5).getMillis();

        // ant-man score
        PointConcept antManWalkKmScore = new PointConcept("Walk_Km", executionMoment);
        long ONE_DAY_MILLIS = 86400000;
        antManWalkKmScore.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
        antManWalkKmScore.setScore(5d);

        PlayerState antManState = new PlayerState("game", "Ant-man");
        antManState.getState().add(antManWalkKmScore);
        playerSrv.saveState(antManState);

        // wasp score
        executionMoment = new DateTime(startDate.getTime()).plusHours(2).getMillis();
        PointConcept waspWalkKmScore = new PointConcept("Walk_Km", executionMoment);
        waspWalkKmScore.addPeriod("weekly", startDate, ONE_DAY_MILLIS);
        waspWalkKmScore.setScore(7d);

        PlayerState waspState = new PlayerState("game", "Wasp");
        waspState.getState().add(waspWalkKmScore);
        playerSrv.saveState(waspState);


        BDDMockito.given(clock.now()).willReturn(date("2018-09-29T09:00:00"));

        GroupChallenge challenge1 = new GroupChallenge();
        challenge1.setChallengeModel(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
        challenge1.setGameId("game");
        challenge1.setInstanceName("competitive_performance_123456");
        challenge1.setEnd(date("2018-09-27T00:00:00"));
        challenge1.setState(ChallengeState.ASSIGNED);
        Attendee antMan = new Attendee();
        antMan.setPlayerId("Ant-man");
        antMan.setRole(Role.GUEST);
        challenge1.getAttendees().add(antMan);
        Attendee wasp = new Attendee();
        wasp.setPlayerId("Wasp");
        wasp.setRole(Role.GUEST);
        challenge1.getAttendees().add(wasp);
        challenge1.setChallengePointConcept(new PointConceptRef("Walk_Km", "weekly"));

        challengeManager.save(challenge1);

        gameSrv.conditionCheckPerformanceGroupChallengesTask();

    }

}
