package eu.trentorise.game.profile.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.response.NewGameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;

import static junit.framework.TestCase.assertTrue;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;


/**
 *
 * @author Luca Piras
 */
public class GameProfileControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_GAME_PROFILE_GAME_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;

    /**
     * Test of activateDeactivatePlugin method, of class GameProfileController.
     * @throws java.lang.Exception
     */
    @Test
    public void testNewGame() throws Exception {
        RestTemplateJsonServiceTestHelper<NewGameResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        NewGameResponse response = helper.executeTest("newGame", 
                                                      BASE_RELATIVE_URL + "/newGame" + FINAL_PART_RELATIVE_URL,
                                                      HttpMethod.POST,
                                                      NewGameResponse.class,
                                                      "{\"game\": {\"name\": \"VIAGGIA ROVERETO\"}}");
        
        if (null != response) {
            assertTrue(response.isSuccess());
            assertEquals(new Integer(135), response.getGame().getId());
        }
    }
}