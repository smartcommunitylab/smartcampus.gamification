package eu.trentorise.challenge.generation;

import static eu.trentorise.challenge.TestConstants.CONTEXT;
import static eu.trentorise.challenge.TestConstants.GAMEID;
import static eu.trentorise.challenge.TestConstants.HOST;
import static eu.trentorise.challenge.TestConstants.INSERT_CONTEXT;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import eu.trentorise.game.challenges.ChallengeFactory;
import eu.trentorise.game.challenges.ChallengesRulesGenerator;
import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.rest.Content;
import eu.trentorise.game.challenges.rest.GamificationEngineRestFacade;
import eu.trentorise.game.challenges.rest.Paginator;
import eu.trentorise.game.challenges.rest.RuleDto;
import eu.trentorise.game.challenges.util.ChallengeRuleRow;
import eu.trentorise.game.challenges.util.ChallengeRules;
import eu.trentorise.game.challenges.util.ChallengeRulesLoader;
import eu.trentorise.game.challenges.util.Matcher;

public class GenerationTest {

    private static final Logger logger = LogManager
	    .getLogger(GenerationTest.class);

    private GamificationEngineRestFacade facade;
    private GamificationEngineRestFacade insertFacade;

    @Before
    public void setup() {
	facade = new GamificationEngineRestFacade(HOST + CONTEXT);
	insertFacade = new GamificationEngineRestFacade(HOST + INSERT_CONTEXT);
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
	Paginator users = facade.readGameState("56e7bf3b570ac89331c37262");

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
	Paginator users = facade.readGameState("56e7bf3b570ac89331c37262");

	ChallengesRulesGenerator crg = new ChallengesRulesGenerator(
		new ChallengeFactory());

	// generate challenges
	for (ChallengeRuleRow challengeSpec : result.getChallenges()) {
	    logger.debug("rules generation for challenge: "
		    + challengeSpec.getName());
	    Matcher matcher = new Matcher(challengeSpec);
	    List<Content> filteredUsers = matcher.match(users);
	    logger.debug("found users: " + filteredUsers.size());
	    String res = crg.generateRules(challengeSpec, filteredUsers);
	    logger.debug("generated rules \n" + res + "\n");

	    assertTrue(!res.isEmpty());

	}
    }

    @Test
    public void generateChallengeRulesAndInsertToGamificationEngine()
	    throws NullPointerException, IllegalArgumentException, IOException,
	    UndefinedChallengeException {
	// load
	ChallengeRules result = ChallengeRulesLoader
		.load("challengesRules.xls");

	assertTrue(result != null && !result.getChallenges().isEmpty());

	// get users from gamification engine
	Paginator users = facade.readGameState("56e7bf3b570ac89331c37262");

	ChallengesRulesGenerator crg = new ChallengesRulesGenerator(
		new ChallengeFactory());

	// generate challenges
	for (ChallengeRuleRow challengeSpec : result.getChallenges()) {
	    logger.debug("rules generation for challenge: "
		    + challengeSpec.getName());
	    Matcher matcher = new Matcher(challengeSpec);
	    List<Content> filteredUsers = matcher.match(users);
	    logger.debug("found users: " + filteredUsers.size());
	    String res = crg.generateRules(challengeSpec, filteredUsers);
	    logger.debug("generated rules \n" + res + "\n");

	    assertTrue(!res.isEmpty());

	    // define rule
	    RuleDto rule = new RuleDto();
	    rule.setContent(res);
	    rule.setName(challengeSpec.getName());
	    // insert rule
	    RuleDto insertedRule = insertFacade.insertGameRule(GAMEID, rule);
	    assertTrue(!insertedRule.getId().isEmpty());

	}
    }
}
