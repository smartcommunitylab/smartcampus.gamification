package eu.trentorise.game.aaa.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;

/**
 *
 * @author Luca Piras
 */
public class GameAuthenticationControllerTest {
    
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public GameAuthenticationControllerTest() {
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
     * Test of authenticate method, of class GameAuthenticationController.
     * @throws java.lang.Exception
     */
    @Test
    public void testAuthenticate() throws Exception {
        RestTemplateJsonServiceTestHelper<GameResponse> helper = new RestTemplateJsonServiceTestHelper<>();
        
        GameResponse response = helper.executeTest("authenticate", 
                                                   IGameConstants.SERVICE_GAME_AAA + "/login" + FINAL_PART_RELATIVE_URL,
                                                   GameResponse.class,
                                                   "{\"username\":\"username\",\"password\":\"password\"}");
        
        if (null != response) {
            assertTrue(response.isSuccess());
        }
    }
}