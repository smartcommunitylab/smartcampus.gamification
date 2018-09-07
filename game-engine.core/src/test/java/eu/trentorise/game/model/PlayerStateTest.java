package eu.trentorise.game.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class PlayerStateTest {

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

    @Test
    public void delete_a_pointConcept_from_state() {
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");

        state.getState().add(new PointConcept("prova"));

        boolean isRemoved = state.removeConcept("prova", PointConcept.class);
        assertThat(isRemoved, is(true));
        assertThat(state.getState(), hasSize(0));
    }

    @Test
    public void delete_a_not_existent_pointConcept_from_state() {
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        state.getState().add(new PointConcept("prova"));
        boolean isRemoved = state.removeConcept("prova", ChallengeConcept.class);
        assertThat(isRemoved, is(false));
        assertThat(state.getState(), hasSize(1));
    }

    @Test
    public void try_delete_a_pointConcept_with_different_name_from_state() {
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        state.getState().add(new PointConcept("prova"));
        state.getState().add(new BadgeCollectionConcept("prova"));
        boolean isRemoved = state.removeConcept("other_name", PointConcept.class);
        assertThat(isRemoved, is(false));
        assertThat(state.getState(), hasSize(2));
    }


    @Test
    public void try_delete_a_pointConcept_with_same_name_of_a_badgeCollection_from_state() {
        PlayerState state = new PlayerState("MY_GAME", "PLAYER_ID");
        state.getState().add(new BadgeCollectionConcept("prova"));
        boolean isRemoved = state.removeConcept("prova", PointConcept.class);
        assertThat(isRemoved, is(false));
        assertThat(state.getState(), hasSize(1));
    }
}


