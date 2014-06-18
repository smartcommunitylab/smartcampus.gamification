package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.response.GamificationPluginsListResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import java.util.List;

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
public class GamePluginControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public GamePluginControllerTest() {
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
     * Test of getGamificationPluginList method, of class GamePluginController.
     */
    @Test
    public void testGetGamificationPluginList() throws Exception {
        RestTemplateJsonServiceTestHelper<GamificationPluginsListResponse> helper = new RestTemplateJsonServiceTestHelper<>();
        
        GamificationPluginsListResponse response = helper.executeTest("testGetGamificationPluginList", 
                                                   BASE_RELATIVE_URL + "/getGamificationPluginList" + FINAL_PART_RELATIVE_URL,
                                                   GamificationPluginsListResponse.class, 
                                                   "");
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<GamificationPlugin> gamificationPlugins = response.getGamificationPlugins();
            assertNotNull(gamificationPlugins);
            assertNotEquals(0, gamificationPlugins.size());
        }
    }
}