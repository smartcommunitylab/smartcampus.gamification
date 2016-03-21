package eu.trentorise.challenge.rest;

import static eu.trentorise.challenge.TestConstants.CONTEXT;
import static eu.trentorise.challenge.TestConstants.GAMEID;
import static eu.trentorise.challenge.TestConstants.HOST;
import static eu.trentorise.challenge.TestConstants.INSERT_CONTEXT;
import static eu.trentorise.challenge.TestConstants.SAVE_ITINERARY;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import eu.trentorise.game.challenges.rest.ExecutionDataDTO;
import eu.trentorise.game.challenges.rest.GamificationEngineRestFacade;
import eu.trentorise.game.challenges.rest.Paginator;
import eu.trentorise.game.challenges.rest.RuleDto;
import eu.trentorise.game.challenges.util.ConverterUtil;
import eu.trentorise.game.challenges.util.JourneyData;

public class RestTest {

    private GamificationEngineRestFacade facade;
    private GamificationEngineRestFacade insertFacade;

    @Before
    public void setup() {
	facade = new GamificationEngineRestFacade(HOST + CONTEXT);
	insertFacade = new GamificationEngineRestFacade(HOST + INSERT_CONTEXT);
    }

    @Test
    public void gameReadGameStateTest() {
	Paginator result = facade.readGameState(GAMEID);
	assertTrue(!result.getContent().isEmpty());
    }

    @Test
    public void gameInsertRuleTest() {
	// define rule
	RuleDto rule = new RuleDto();
	rule.setContent("rule \"ss\" when then System.out.println(\"LOGGO\"");
	rule.setName("sampleRule");
	// insert rule
	RuleDto result = insertFacade.insertGameRule(GAMEID, rule);
	assertTrue(!result.getId().isEmpty());
	// delete inserted rule
	boolean res = insertFacade.deleteGameRule(GAMEID, result.getId());
	assertTrue(res);
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
    public void saveUsersItineraryLoadedFromFile() throws IOException {
	// create input
	String ref = "testTrips1.json";
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
