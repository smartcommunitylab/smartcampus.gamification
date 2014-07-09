package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.response.GamificationPluginListResponse;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import java.util.ArrayList;
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
        RestTemplateJsonServiceTestHelper<GamificationPluginListResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        GamificationPluginListResponse response = helper.executeTest("testGetGamificationPluginList", 
                                                  BASE_RELATIVE_URL + "/getGamificationPluginList" + FINAL_PART_RELATIVE_URL,
                                                  GamificationPluginListResponse.class, 
                                                  "");
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<GamificationPlugin> gamificationPlugins = response.getGamificationPlugins();
            assertNotNull(gamificationPlugins);
            assertNotEquals(0, gamificationPlugins.size());
        }
    }
    
    /**
     * Test of getCustomizedGamificationPluginList method, of class GamePluginController.
     */
    @Test
    public void testGetCustomizedGamificationPluginList() throws Exception {
        RestTemplateJsonServiceTestHelper<GamificationPluginListResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        MockGamePluginManager mock = new MockGamePluginManager();
        
        
        GamificationPluginListResponse response = this.executeTest(helper, new GamificationPlugin());
        List<GamificationPlugin> list = new ArrayList<>();
        this.testCustomizedPlugins(response, list);
        
        response = this.executeTest(helper, mock.createPointsPlugin());
        list = new ArrayList<>();
        list.add(mock.createGreenLeavesPointPlugin());
        list.add(mock.createHeartsPointPlugin());
        list.add(mock.createUsagePointsPointPlugin());
        this.testCustomizedPlugins(response, list);
        
        response = this.executeTest(helper, mock.createBadgeCollectionPlugin());
        list = new ArrayList<>();
        list.add(mock.createUsageBadgesPlugin());
        list.add(mock.createHealthBadgesPlugin());
        list.add(mock.createEcologicalBadgesPlugin());
        this.testCustomizedPlugins(response, list);
        
        response = this.executeTest(helper, mock.createPointLeadearboardPlugin());
        list = new ArrayList<>();
        list.add(mock.createGreenWeeklyLeadearboardPlugin());
        list.add(mock.createGreenMonthlyLeadearboardPlugin());
        list.add(mock.createUsageCumulativeLeadearboardPlugin());
        this.testCustomizedPlugins(response, list);
    }
    
    protected String makeRequest(GamificationPlugin plugin) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("{\"gamificationPlugin\": {");
        sb.append("                          \"name\": \"").append(plugin.getName()).append("\",");
        sb.append("                          \"version\": \"\"");
        sb.append("                          }");
        sb.append("}");
        
        return sb.toString();
    }

    protected GamificationPluginListResponse executeTest(RestTemplateJsonServiceTestHelper<GamificationPluginListResponse> helper, 
                                                         GamificationPlugin gamificationPlugin) throws Exception {
        
        return helper.executeTest("testGetCustomizedGamificationPluginList", 
                                  BASE_RELATIVE_URL + "/getCustomizedGamificationPluginList" + FINAL_PART_RELATIVE_URL,
                                  GamificationPluginListResponse.class, 
                                  this.makeRequest(gamificationPlugin));
    }

    protected void testCustomizedPlugins(GamificationPluginListResponse response, 
                                         List<GamificationPlugin> elements) {
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<GamificationPlugin> responsePlugins = response.getGamificationPlugins();
            assertNotNull(responsePlugins);
            assertEquals(elements.size(), responsePlugins.size());
            
            for (int i = 0; i < responsePlugins.size(); i++) {
                GamificationPlugin responsePlugin = responsePlugins.get(i);
                GamificationPlugin element = elements.get(i);
                
                assertEquals(responsePlugin.getName(), element.getName());
            }
        }
    }
}