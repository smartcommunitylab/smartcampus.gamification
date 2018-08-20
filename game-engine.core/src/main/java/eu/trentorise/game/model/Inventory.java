package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<ChallengeChoice> challengeChoices = new ArrayList<>();

    public Inventory() {}

    public Inventory add(ChallengeChoice challengeChoice) {
        if (challengeChoice != null && !challengeChoices.contains(challengeChoice)) {
            challengeChoices.add(challengeChoice);
        }

        return this;
    }

    public int size() {
        return challengeChoices.size();
    }

    public List<ChallengeChoice> getChallengeChoices() {
        return challengeChoices;
    }
}
