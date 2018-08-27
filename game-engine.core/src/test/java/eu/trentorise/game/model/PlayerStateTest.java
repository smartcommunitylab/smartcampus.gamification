package eu.trentorise.game.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.model.ChallengeChoice.ChoiceState;
import eu.trentorise.game.model.Inventory.ItemChoice;
import eu.trentorise.game.model.Inventory.ItemChoice.ChoiceType;
import eu.trentorise.game.model.Level.Config;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class PlayerStateTest {

    @Autowired
    private PlayerService playerSrv;

    // @Test
    // public void add_to_inventory() {
    // PlayerState state = new PlayerState();
    // ChallengeChoice challengeChoice = new ChallengeChoice();
    // state.getInventory().add(challengeChoice);
    // assertThat(state.getInventory().size(), is(1));
    // }


    // @Test
    // public void add_second_item_to_inventory() {
    // PlayerState state = new PlayerState();
    // ChallengeChoice challengeChoice = new ChallengeChoice();
    // state.getInventory().add(challengeChoice);
    //
    // ChallengeChoice secondChallengeChoice =
    // new ChallengeChoice("percentageIncrement", ChoiceState.AVAILABLE, null);
    // state.getInventory().add(secondChallengeChoice);
    // assertThat(state.getInventory().size(), is(2));
    // }

    // @Test
    // public void an_already_inserted_item_should_not_be_added() {
    // PlayerState state = new PlayerState();
    // ChallengeChoice challengeChoice =
    // new ChallengeChoice("absoluteIncrement", ChoiceState.AVAILABLE, null);
    // state.getInventory().add(challengeChoice);
    // assertThat(state.getInventory().size(), is(1));
    // ChallengeChoice otherChallengeChoice =
    // new ChallengeChoice("absoluteIncrement", ChoiceState.AVAILABLE, null);
    // state.getInventory().add(otherChallengeChoice);
    // assertThat(state.getInventory().size(), is(1));
    // }

    // active item

    // @Test
    // public void persist_state() {
    // String gameId = "MY_GAME";
    // String playerId = "PLAYER_ID";
    // PlayerState state = new PlayerState(gameId, playerId);
    // ChallengeChoice challengeChoice =
    // new ChallengeChoice("absoluteIncrement", ChoiceState.AVAILABLE, null);
    // state.getInventory().add(challengeChoice);
    //
    // PlayerState saved = playerSrv.saveState(state);
    // assertThat(saved, notNullValue());
    // assertThat(saved.getInventory().size(), is(1));
    //
    // }

    // @Test
    // public void load_persisted_state() {
    // String gameId = "MY_GAME";
    // String playerId = "PLAYER_ID";
    // PlayerState state = new PlayerState(gameId, playerId);
    // ChallengeChoice challengeChoice =
    // new ChallengeChoice("absoluteIncrement", ChoiceState.AVAILABLE, null);
    // state.getInventory().add(challengeChoice);
    //
    // playerSrv.saveState(state);
    // PlayerState saved = playerSrv.loadState(gameId, playerId, false);
    // assertThat(saved, notNullValue());
    // assertThat(saved.getInventory().size(), is(1));
    // }

    @Test
    public void inventory_should_be_empty_if_levels_not_exist() {
        Game game = new Game("MY_GAME");
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        state.updateInventory(game, null);
        assertThat(state.getInventory().size(), is(0));
    }

    @Test
    public void inventory_should_be_empty_if_level_without_configs() {
        Game game = new Game("MY_GAME");
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold threshold = new Threshold("walker", 300d);
        levelDefinition.getThresholds().add(threshold);

        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        List<PlayerLevel> levels = new ArrayList<>();
        levels.add(new PlayerLevel(levelDefinition, 300d));
        state.updateLevels(levels);
        
        state.updateInventory(game, null);
        assertThat(state.getInventory().size(), is(0));
    }

    @Test
    public void inventory_has_size_1_when_player_has_level_with_configs() {
        Game game = new Game("MY_GAME");
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold threshold = new Threshold("walker", 300d);
        Config levelConfig = new Config();
        levelConfig.setChoices(1);
        levelConfig.getAvailableModels().add("absoluteIncrement");
        threshold.setConfig(levelConfig);
        levelDefinition.getThresholds().add(threshold);

        game.getLevels().add(levelDefinition);
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        List<PlayerLevel> levels = new ArrayList<>();
        levels.add(new PlayerLevel(levelDefinition, 300d));
        state.updateLevels(levels);

        state.updateInventory(game, Arrays.asList(new LevelInstance("greener", "walker")));
        assertThat(state.getInventory().size(), is(1));
    }

    @Test
    public void inventory_should_be_empty_when_player_hasnt_level_with_configs() {
        Game game = new Game("MY_GAME");
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold threshold = new Threshold("walker", 300d);
        levelDefinition.getThresholds().add(threshold);
        Threshold masterThreshold = new Threshold("master", 500d);
        Config levelConfig = new Config();
        levelConfig.setChoices(1);
        levelConfig.getAvailableModels().add("absoluteIncrement");
        masterThreshold.setConfig(levelConfig);
        levelDefinition.getThresholds().add(masterThreshold);

        game.getLevels().add(levelDefinition);
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        List<PlayerLevel> levels = new ArrayList<>();
        levels.add(new PlayerLevel(levelDefinition, 320d));
        state.updateLevels(levels);

        state.updateInventory(game, Arrays.asList(new LevelInstance("greener", "walker")));
        assertThat(state.getInventory().size(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void game_passed_to_updateInventory_should_be_the_same_of_the_state() {
        Game game = new Game("MY_GAME");
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold threshold = new Threshold("walker", 300d);
        Config levelConfig = new Config();
        levelConfig.setChoices(1);
        levelConfig.getAvailableModels().add("absoluteIncrement");
        threshold.setConfig(levelConfig);
        levelDefinition.getThresholds().add(threshold);

        PlayerState state = new PlayerState("ANOTHER_GAME", "PLAYER_ID");
        List<PlayerLevel> levels = new ArrayList<>();
        levels.add(new PlayerLevel(levelDefinition, 310d));
        state.updateLevels(levels);

        state.updateInventory(game, Arrays.asList(new LevelInstance("greener", "walker")));
    }

    @Test
    public void inventory_should_contains_available_items_of_two_levels_gained_in_same_action() {
        Game game = new Game("MY_GAME");
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold walkerLevel = new Threshold("walker", 100d);
        Config levelConfig = new Config();
        levelConfig.setChoices(1);
        levelConfig.getAvailableModels().add("absoluteIncrement");
        walkerLevel.setConfig(levelConfig);
        levelDefinition.getThresholds().add(walkerLevel);

        Threshold runnerLevel = new Threshold("runner", 200d);
        Config runnerLevelConfig = new Config();
        runnerLevelConfig.setChoices(1);
        runnerLevelConfig.getAvailableModels().add("runnerChallengeType");
        runnerLevel.setConfig(runnerLevelConfig);
        levelDefinition.getThresholds().add(runnerLevel);

        game.getLevels().add(levelDefinition);

        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        List<PlayerLevel> levels = new ArrayList<>();
        levels.add(new PlayerLevel(levelDefinition, 200d));
        state.updateLevels(levels);

        state.updateInventory(game, Arrays.asList(new LevelInstance("greener", "walker"),
                new LevelInstance("greener", "runner")));

        assertThat(state.getInventory().size(), is(2));
        List<String> availableModelNames = state.getInventory().getChallengeChoices().stream()
                .map(choice -> choice.getModelName()).collect(Collectors.toList()); 
        assertThat(availableModelNames, hasItems("absoluteIncrement", "runnerChallengeType"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void activate_not_existent_challenge_choice() {
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        final Inventory inventory = state.getInventory();

        Config levelConfig = new Config();
        levelConfig.getAvailableModels().add("model2");
        levelConfig.setChoices(1);
        inventory.upgrade(levelConfig);

        inventory.activateChoice(new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1"));
    }

    @Test
    public void activate_challenge_choice() {
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        final Inventory inventory = state.getInventory();

        Config levelConfig = new Config();
        levelConfig.getAvailableModels().add("model1");
        levelConfig.setChoices(1);
        inventory.upgrade(levelConfig);

        inventory.activateChoice(new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1"));
        assertThat(inventory.getChallengeChoices().size(), is(1));
        assertThat(inventory.getChallengeChoices().get(0).getState(),
                is(ChoiceState.ACTIVE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void no_more_activation_actions() {
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        final Inventory inventory = state.getInventory();

        Config levelConfig = new Config();
        levelConfig.getAvailableModels().add("model1");
        levelConfig.getAvailableModels().add("model2");
        levelConfig.setChoices(1);
        inventory.upgrade(levelConfig);

        inventory.activateChoice(new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model2"));
        inventory.activateChoice(new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1"));

    }
}


