package eu.trentorise.game.managers;

import java.util.HashSet;

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
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.GameService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class GameManagerTest {

    @Autowired
    private GameService gameSrv;


    @Autowired
    private MongoTemplate mongo;

    @Before
    public void setup() {
        mongo.getDb().dropDatabase();
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

}
