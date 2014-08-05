package eu.trentorise.game.plugin.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.point.request.PointPluginRequest;
import eu.trentorise.game.plugin.response.CustomizedPluginResponse;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
public class PointPluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_POINT_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public PointPluginControllerTest() {
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
     * Test of setCustomizedGamificationPlugin method, of class PointPluginController.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetCustomizedGamificationPlugin() throws Exception {
        RestTemplateJsonServiceTestHelper<CustomizedPluginResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        MockPluginManager mock = new MockPluginManager();
        ObjectMapper mapper = new ObjectMapper();
        
        Game game = new Game();
        game.setId(135);
        
        PointPluginRequest request = new PointPluginRequest();
        request.setGame(game);
        PointPlugin plugin = mock.createGreenLeavesPointPlugin();
        plugin.setId(null);
        request.setPlugin(plugin);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        CustomizedPluginResponse response = helper.executeTest("testPointSetCustomizedGamificationPlugin", 
                                                                           BASE_RELATIVE_URL + "/setCustomizedGamificationPlugin" + FINAL_PART_RELATIVE_URL,
                                                                           HttpMethod.POST,
                                                                           CustomizedPluginResponse.class, 
                                                                           jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            CustomizedPlugin customizedPlugin = response.getCustomizedPlugin();
            
            assertNotNull(customizedPlugin.getId());
            assertEquals(customizedPlugin.getGamificationPlugin().getId(), plugin.getGamificationPlugin().getId());
            assertEquals(customizedPlugin.getName(), plugin.getName());
            assertEquals(customizedPlugin.getVersion(), plugin.getVersion());
            assertEquals(customizedPlugin.getDescription(), plugin.getDescription());
        }
    }
}