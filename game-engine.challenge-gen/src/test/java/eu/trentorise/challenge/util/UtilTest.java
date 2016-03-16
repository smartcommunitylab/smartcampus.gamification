package eu.trentorise.challenge.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import eu.trentorise.game.challenges.util.ChallengeRules;
import eu.trentorise.game.challenges.util.ChallengeRulesLoader;

public class UtilTest {

    @Test(expected = NullPointerException.class)
    public void loadNullTest() throws NullPointerException,
	    IllegalArgumentException, IOException {
	ChallengeRulesLoader.load(null);
    }

    @Test
    public void loadChallengesRulesTest() throws IOException {
	ChallengeRules result = ChallengeRulesLoader
		.load("challengesRules.xls");

	assertTrue(!result.getChallenges().isEmpty());
    }

}
