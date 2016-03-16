package eu.trentorise.challenge.genenration;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.trentorise.game.challenges.rest.Content;
import eu.trentorise.game.challenges.rest.GamificationEngineRestFacade;
import eu.trentorise.game.challenges.rest.Paginator;
import eu.trentorise.game.challenges.util.ChallengeRules;
import eu.trentorise.game.challenges.util.ChallengeRulesLoader;
import eu.trentorise.game.challenges.util.Matcher;

public class GenerationTest {

    private GamificationEngineRestFacade facade;

    @Before
    public void setup() {
	facade = new GamificationEngineRestFacade(
		"http://localhost:8080/gamification/gengine");
    }

    @Test
    public void loadChallengeRuleGenerate() throws NullPointerException,
	    IllegalArgumentException, IOException {
	// load
	ChallengeRules result = ChallengeRulesLoader
		.load("challengesRules.xls");

	assertTrue(result != null && !result.getChallenges().isEmpty());

	// get users from gamification engine
	Paginator users = facade.gameState("56e7bf3b570ac89331c37262");

	// generate challenges
	Matcher matcher = new Matcher(result.getChallenges().get(0));
	List<Content> r = matcher.match(users);

	assertTrue(!r.isEmpty());
    }
}
