package eu.trentorise.game.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ChallengeChoice {
    private String modelName;
    private ChoiceState state;
    private LevelInstance activeAtLevel;


    public enum ChoiceState {
        AVAILABLE, ACTIVE
    }

    public ChallengeChoice() {}

    public ChallengeChoice(String modelName, ChoiceState choiceState, LevelInstance activeAtLevel) {
        this.modelName = modelName;
        this.state = choiceState;
        this.activeAtLevel = activeAtLevel;
    }

    public String getModelName() {
        return modelName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }
        ChallengeChoice rhs = (ChallengeChoice) obj;
        return new EqualsBuilder().append(modelName, rhs.modelName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(21, 3).append(modelName).hashCode();
    }

    public ChoiceState getState() {
        return state;
    }
}
