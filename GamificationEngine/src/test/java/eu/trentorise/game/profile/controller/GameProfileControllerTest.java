package eu.trentorise.game.profile.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.response.NewGameResponse;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;

import static junit.framework.TestCase.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author Luca Piras
 */
public class GameProfileControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_GAME_PROFILE_GAME;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public GameProfileControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of activateDeactivatePlugin method, of class GameProfileController.
     * @throws java.lang.Exception
     */
    @Test
    public void testNewGame() throws Exception {
        RestTemplateJsonServiceTestHelper<NewGameResponse> helper = new RestTemplateJsonServiceTestHelper<>();
        
        NewGameResponse response = helper.executeTest("newGame", 
                                                      BASE_RELATIVE_URL + "/newGame" + FINAL_PART_RELATIVE_URL,
                                                      NewGameResponse.class,
                                                      "{\"gameName\":\"VIAGGIA ROVERETO\"}");
        
        if (null != response) {
            assertTrue(response.isSuccess());
            assertEquals(new Integer(135), response.getNewGameId());
        }
    }
}