package eu.trentorise.game.configuration.controller;;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;

import static junit.framework.TestCase.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author Luca Piras
 */
public class GameConfigurationControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_GAME_CONFIGURATION;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public GameConfigurationControllerTest() {
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
    /*@Test
    public void importGamifiableActions() throws Exception {
        RestTemplateJsonServiceTestHelper<GameResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        GameResponse response = helper.executeTest("activateDeactivatePlugin", 
                                                   BASE_RELATIVE_URL + "/activateDeactivatePlugin" + FINAL_PART_RELATIVE_URL,
                                                   GameResponse.class,
                                                   "{\"gameProfileId\":\"1\",\"gamificationPluginId\":\"2\",\"active\":\"true\"}");
        
        if (null != response) {
            assertTrue(response.isSuccess());
        }
    }
        //TODO: implement the related test
        //At the moment you can test it with the following html:
    
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>

        </head>
            <body>
                <div class="container">
                        <form action="http://localhost:8080/gamificationengine/game/services/configuration/importGamifiableActions.service" method="post" enctype="multipart/form-data">
                                <input type="text" name="gameId" size="50" />		    
                                <p>Select a file : <input type="file" name="gamifiableActionsFile" size="50" /></p>

                            <input type="submit" value="Upload" />
                        </form>
                </div>
            </body>
        </head>
    */
    
    /**
     * Test of activateDeactivatePlugin method, of class GameProfileController.
     * @throws java.lang.Exception
     */
    /*@Test
    public void testActivateDeactivatePlugin() throws Exception {
        RestTemplateJsonServiceTestHelper<GameResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        GameResponse response = helper.executeTest("activateDeactivatePlugin", 
                                                   BASE_RELATIVE_URL + "/activateDeactivatePlugin" + FINAL_PART_RELATIVE_URL,
                                                   GameResponse.class,
                                                   "{\"gameProfileId\":\"1\",\"gamificationPluginId\":\"2\",\"active\":\"true\"}");
        
        if (null != response) {
            assertTrue(response.isSuccess());
        }
    }*/
}