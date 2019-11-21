package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.trentorise.game.model.ChallengeChoice.ChoiceState;
import eu.trentorise.game.model.Inventory.ItemChoice.ChoiceType;
import eu.trentorise.game.model.Level.Config;

public class Inventory {

    private List<ChallengeChoice> challengeChoices = new ArrayList<>();

    private int challengeActivationActions;

    public Inventory() {}

    public static class ItemChoice {
        private ChoiceType type;
        private String name;

        public ItemChoice() {

        }

        public ItemChoice(ChoiceType type, String name) {
            this.type = type;
            this.name = name;
        }

        public enum ChoiceType {
            CHALLENGE_MODEL
        }


        public ChoiceType getType() {
            return type;
        }

        public void setType(ChoiceType type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public Inventory upgrade(Config levelConfig) {
        if (levelConfig != null) {
            Stream<ChallengeChoice> availableChoiceStream = levelConfig.getAvailableModels()
                    .stream().map(availableModel -> new ChallengeChoice(availableModel,
                            ChoiceState.AVAILABLE));
            Stream<ChallengeChoice> activeChoiceStream = levelConfig.getActiveModels().stream()
                    .map(availableModel -> new ChallengeChoice(availableModel, ChoiceState.ACTIVE));
            List<ChallengeChoice> levelChoices = Stream
                    .concat(availableChoiceStream, activeChoiceStream)
                    .collect(Collectors.toList());
            challengeChoices.addAll(levelChoices);
            challengeActivationActions += levelConfig.getChoices();
        }

        return this;
    }

    public Inventory activateChoice(ItemChoice choice) {
        if (challengeActivationActions == 0) {
            throw new IllegalArgumentException("No activation actions available");
        }
        boolean found = false;
        if (isChallengeChoice(choice)) {
            for (ChallengeChoice challengeChoice : challengeChoices) {
                if (challengeChoice.getModelName().equals(choice.getName())) {
                    found = true;
                    if (challengeChoice.getState() == ChoiceState.AVAILABLE) {
                        challengeChoice.update(ChoiceState.ACTIVE);
                        challengeActivationActions--;
                    }
                }
            }
            if (!found) {
                throw new IllegalArgumentException(String
                        .format("Choice %s is not available for the player", choice.getName()));
            }
        }
        return this;
    }

    public int size() {
        return challengeChoices.size();
    }

    public List<ChallengeChoice> getChallengeChoices() {
        return challengeChoices;
    }

    public int getChallengeActivationActions() {
        return challengeActivationActions;
    }

    private boolean isChallengeChoice(ItemChoice choice) {
        return choice != null && choice.getType() == ChoiceType.CHALLENGE_MODEL;
    }
}
