package eu.trentorise.game.aaa.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
public class GameAuthenticationControllerTest extends SkipServiceTestHelper {
    
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;

    /**
     * Test of authenticate method, of class GameAuthenticationController.
     * @throws java.lang.Exception
     */
    @Test
    public void testAuthenticate() throws Exception {
        RestTemplateJsonServiceTestHelper<GameResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        GameResponse response = helper.executeTest("authenticate", 
                                                   IGameConstants.SERVICE_GAME_AAA_PATH + "/login" + FINAL_PART_RELATIVE_URL,
                                                   HttpMethod.POST,
                                                   GameResponse.class,
                                                   "{\"username\":\"username\",\"password\":\"password\"}");
        
        if (null != response) {
            assertTrue(response.isSuccess());
        }
    }
}