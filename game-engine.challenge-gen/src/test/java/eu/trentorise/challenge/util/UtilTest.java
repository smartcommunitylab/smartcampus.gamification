package eu.trentorise.challenge.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import eu.trentorise.game.challenges.util.ChallengeRules;
import eu.trentorise.game.challenges.util.ChallengeRulesLoader;
import eu.trentorise.game.challenges.util.ConverterUtil;
import eu.trentorise.game.challenges.util.JourneyData;

public class UtilTest {

    @Test(expected = NullPointerException.class)
    public void loadNullTest() throws NullPointerException,
	    IllegalArgumentException, IOException {
	ChallengeRulesLoader.load(null);
    }

    @Test
    public void loadChallengesRulesTest() throws IOException {
	ChallengeRules result = ChallengeRulesLoader
		.load("challengesRules.csv");

	assertTrue(!result.getChallenges().isEmpty());
    }

    @Test
    public void readUserData() throws IOException {
	String ref = "savedtrips1.json";
	// read all lines from file
	List<String> lines = IOUtils.readLines(Thread.currentThread()
		.getContextClassLoader().getResourceAsStream(ref));

	assertTrue(lines != null);

	for (String line : lines) {
	    JourneyData jd = ConverterUtil.extractJourneyData(line);
	    assertTrue(jd != null && !jd.getData().isEmpty());
	}
    }

}
