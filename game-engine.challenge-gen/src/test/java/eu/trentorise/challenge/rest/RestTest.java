package eu.trentorise.challenge.rest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.trentorise.game.challenges.rest.GamificationEngineRestFacade;
import eu.trentorise.game.challenges.rest.Paginator;
import eu.trentorise.game.challenges.rest.RuleDto;

public class RestTest {

    private final static String HOST = "http://localhost:8080/gamification/";
    private final static String READ_CONTEXT = "gengine/";
    private final static String INSERT_CONTEXT = "console/";
    private static final String GAMEID = "56e7bf3b570ac89331c37262";

    @Test
    public void gameReadGameStateTest() {
	GamificationEngineRestFacade facade = new GamificationEngineRestFacade(
		HOST + READ_CONTEXT);
	Paginator result = facade.readGameState(GAMEID);
	assertTrue(!result.getContent().isEmpty());
    }

    @Test
    public void gameInsertRuleTest() {
	GamificationEngineRestFacade facade = new GamificationEngineRestFacade(
		HOST + INSERT_CONTEXT);
	// define rule
	RuleDto rule = new RuleDto();
	rule.setContent("rule \"ss\" when then System.out.println(\"LOGGO\"");
	rule.setName("sampleRule");
	// insert rule
	RuleDto result = facade.insertGameRule(GAMEID, rule);
	assertTrue(!result.getId().isEmpty());
	// delete inserted rule
	boolean res = facade.deleteGameRule(GAMEID, result.getId());
	assertTrue(res);
    }
}
