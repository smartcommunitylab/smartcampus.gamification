package eu.trentorise.challenge.rest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import eu.trentorise.game.challenges.rest.ExecutionDataDTO;
import eu.trentorise.game.challenges.rest.GamificationEngineRestFacade;
import eu.trentorise.game.challenges.rest.Paginator;
import eu.trentorise.game.challenges.rest.RuleDto;
import eu.trentorise.game.challenges.util.ConverterUtil;
import eu.trentorise.game.challenges.util.JourneyData;

public class RestTest {

    private static final String SAVE_ITINERARY = "save_itinerary";
    private final static String HOST = "http://localhost:8080/gamification/";
    private final static String CONTEXT = "gengine/";
    private final static String INSERT_CONTEXT = "console/";
    private static final String GAMEID = "56cc77737d847cb2a12ea89b";

    @Test
    public void gameReadGameStateTest() {
	GamificationEngineRestFacade facade = new GamificationEngineRestFacade(
		HOST + CONTEXT);
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
//	boolean res = facade.deleteGameRule(GAMEID, result.getId());
//	assertTrue(res);
    }

    @Test
    public void saveItineraryTest() {
	GamificationEngineRestFacade facade = new GamificationEngineRestFacade(
		HOST + CONTEXT);
	ExecutionDataDTO input = new ExecutionDataDTO();
	input.setActionId(SAVE_ITINERARY);
	input.setPlayerId("1");
	input.setGameId(GAMEID);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("bikeDistance", Double.valueOf(1.0d));
	input.setData(data);
	boolean result = facade.saveItinerary(input);

	assertTrue(result);
    }

    @Test
    public void saveUsersItineraryLoadedFromFil() throws IOException {
	// init facade
	GamificationEngineRestFacade facade = new GamificationEngineRestFacade(
		HOST + CONTEXT);
	// create input
	String ref = "testTrips129.json";
	// read all lines from file
	List<String> lines = IOUtils.readLines(Thread.currentThread()
		.getContextClassLoader().getResourceAsStream(ref));

	for (String line : lines) {
	    JourneyData jd = ConverterUtil.extractJourneyData(line);
	    ExecutionDataDTO input = new ExecutionDataDTO();
	    input.setActionId(SAVE_ITINERARY);
	    input.setPlayerId(jd.getUserId());
	    input.setGameId(GAMEID);
	    input.setData(jd.getData());
	    boolean result = facade.saveItinerary(input);

	    assertTrue(result);

	}

    }
}
