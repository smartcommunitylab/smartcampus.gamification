package eu.trentorise.game.challenges.util;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRules {

    private List<ChallengeRuleRow> challenges = new ArrayList<ChallengeRuleRow>();

    public List<ChallengeRuleRow> getChallenges() {
	return challenges;
    }

    public void setChallenges(List<ChallengeRuleRow> challenges) {
	this.challenges = challenges;
    }

}
