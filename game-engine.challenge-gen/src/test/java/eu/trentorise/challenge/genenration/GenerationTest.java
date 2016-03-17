package eu.trentorise.challenge.genenration;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;
import eu.trentorise.game.challenges.rest.Content;
import eu.trentorise.game.challenges.rest.GamificationEngineRestFacade;
import eu.trentorise.game.challenges.rest.Paginator;
import eu.trentorise.game.challenges.util.ChallengeFactory;
import eu.trentorise.game.challenges.util.ChallengeRuleRow;
import eu.trentorise.game.challenges.util.ChallengeRules;
import eu.trentorise.game.challenges.util.ChallengeRulesLoader;
import eu.trentorise.game.challenges.util.Matcher;

public class GenerationTest {

    private GamificationEngineRestFacade facade;
    private ChallengeFactory factory;
    private HashMap<String, Object> params;

    @Before
    public void setup() {
	facade = new GamificationEngineRestFacade(
		"http://localhost:8080/gamification/gengine");
	factory = new ChallengeFactory();
    }

    @Test
    public void loadChallengeRuleGenerate() throws NullPointerException,
	    IllegalArgumentException, IOException {
	// load
	ChallengeRules result = ChallengeRulesLoader
		.load("challengesRules.xls");

	assertTrue(result != null && !result.getChallenges().isEmpty());

	// get users from gamification engine
	// TODO paginazione risultati da gamification engine
	Paginator users = facade.gameState("56e7bf3b570ac89331c37262");

	// generate challenges
	Matcher matcher = new Matcher(result.getChallenges().get(0));
	List<Content> r = matcher.match(users);

	assertTrue(!r.isEmpty());
    }

    @Test
    public void loadTestGeneration() throws NullPointerException,
	    IllegalArgumentException, IOException, UndefinedChallengeException {
	// load
	ChallengeRules result = ChallengeRulesLoader
		.load("challengesRules.xls");

	assertTrue(result != null && !result.getChallenges().isEmpty());

	// get users from gamification engine
	Paginator users = facade.gameState("56e7bf3b570ac89331c37262");

	StringBuffer buffer;
	// generate challenges
	for (ChallengeRuleRow challengeSpec : result.getChallenges()) {
	    // filtro utenti per la challenge specifica
	    Matcher matcher = new Matcher(challengeSpec);
	    List<Content> r = matcher.match(users);

	    buffer = new StringBuffer();
	    buffer.append("/** " + challengeSpec.getType() + " "
		    + challengeSpec.getTarget().toString() + " **/\n");

	    // get right challenge
	    for (Content user : r) {
		Challenge c = factory.createChallenge(ChallengeType
			.valueOf(challengeSpec.getType()));
		params = new HashMap<String, Object>();
		if (challengeSpec.getTarget() instanceof Double) {
		    params.put("percent", challengeSpec.getTarget());
		}
		params.put("mode", challengeSpec.getGoalType());
		params.put("bonus", challengeSpec.getBonus());
		params.put("point_type", challengeSpec.getPointType());
		params.put("baseline", user.getCustomData()
			.getAdditionalProperties().get("walk_km_past"));
		c.setTemplateParams(params);
		c.compileChallenge(user.getPlayerId());

		Assert.assertTrue("Challenge " + ChallengeType.PERCENT
			+ " created", c != null);
		Assert.assertTrue(c.getType().equals(ChallengeType.PERCENT));
		Assert.assertTrue(c.getGeneratedRules() != null
			&& !c.getGeneratedRules().equals(""));
		// System.out.println(c.getGeneratedRules() + "\n\n");
		buffer.append(c.getGeneratedRules());
	    }
	    assertTrue(buffer != null && !buffer.toString().isEmpty());
	    System.out.println(buffer.toString() + "\n\n");
	}
    }
}
