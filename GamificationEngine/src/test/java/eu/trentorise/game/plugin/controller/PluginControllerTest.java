package eu.trentorise.game.plugin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.comparator.PluginKeyComparator;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.request.CustomizedPluginActivationDeactivationRequest;
import eu.trentorise.game.plugin.response.CustomizedPluginListResponse;
import eu.trentorise.game.plugin.response.PluginCollectionResponse;
import eu.trentorise.game.plugin.response.PluginResponse;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;


/**
 *
 * @author Luca Piras
 */
public class PluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;

    
    @Test
    public void testReadPlugins() throws Exception {
        Collection<Plugin> expectedElements = MockPluginManager.createInstance().createElements();
        this.executeTestReadPlugins((List<Plugin>) expectedElements);
    }
    
    protected void executeTestReadPlugins(List<Plugin> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<PluginCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        PluginCollectionResponse response = helper.executeTest("PluginControllerTest - testReadPlugins",
                                                               BASE_RELATIVE_URL,
                                                               HttpMethod.GET,
                                                               PluginCollectionResponse.class, 
                                                               null);
        
        if (null != response) {
            List<Plugin> responseElements = (List) response.getPlugins();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                Plugin responseElement = responseElements.get(i);
                Plugin expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getId(), expectedElement.getId());
                assertEquals(responseElement.getName(), 
                             expectedElement.getName());
            }
        }
    }
    
    @Test
    public void testReadPluginById() throws Exception {
        Plugin expectedElement = MockPluginManager.createInstance().createPointsPlugin();
        this.executeTestReadPluginById(expectedElement, HttpStatus.OK);
        
        expectedElement.setId(-1);
        this.executeTestReadPluginById(expectedElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestReadPluginById(Plugin expectedElement, 
                                             HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<PluginResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        PluginResponse response = helper.executeTest("PluginControllerTest - testReadPluginById",
                                                     BASE_RELATIVE_URL + "/" + expectedElement.getId(),
                                                     HttpMethod.GET,
                                                     PluginResponse.class,
                                                     null, 
                                                     expectedStatus);
        
        if (null != response && 0 == expectedStatus.compareTo(HttpStatus.OK)) {
            Plugin responseElement = response.getPlugin();
            
            assertNotNull(responseElement);
            assertEquals(0, (new PluginKeyComparator()).compare(expectedElement, responseElement));
        }
    }
    
    /**
     * Test of getCustomizedGamificationPlugins method, of class GamePluginController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetCustomizedGamificationPlugins() throws Exception {
        RestTemplateJsonServiceTestHelper<CustomizedPluginListResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        MockPluginManager mock = new MockPluginManager();
        
        
        CustomizedPluginListResponse response = this.executeTest(helper, new Plugin());
        List<CustomizedPlugin> list = new ArrayList<>();
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
        
        response = this.executeTest(helper, mock.createLeadearboardPointPlugin());
        list = new ArrayList<>();
        list.add(mock.createGreenWeeklyLeadearboardPlugin());
        list.add(mock.createGreenMonthlyLeadearboardPlugin());
        list.add(mock.createUsageCumulativeLeadearboardPlugin());
        this.testCustomizedPlugins(response, list);
    }
    
    /**
     * Test of activateDeactivateCustomizedGamificationPlugin method, of class GamePluginController.
     */
    @Test
    public void testActivateDeactivateCustomizedGamificationPlugin() throws Exception {
        RestTemplateJsonServiceTestHelper<GameResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        MockPluginManager mock = new MockPluginManager();
        ObjectMapper mapper = new ObjectMapper();
        
        CustomizedPluginActivationDeactivationRequest request = new CustomizedPluginActivationDeactivationRequest();
        
        Game game = new Game();
        game.setId(135);
        
        request.setGame(game);
        request.setCustomizedGamificationPlugin(mock.createEcologicalBadgesPlugin());
        request.setActivated(true);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        GameResponse response = helper.executeTest("testActivateDeactivateCustomizedGamificationPlugin", 
                                                   BASE_RELATIVE_URL + "/activateDeactivateCustomizedGamificationPlugin" + FINAL_PART_RELATIVE_URL,
                                                   HttpMethod.POST,
                                                   GameResponse.class, 
                                                   jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
        }
    }
    
    protected String makeRequest(Plugin plugin) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("{\"game\": {");
        sb.append("            \"id\": 135");
        sb.append("           },");
        sb.append(" \"gamificationPlugin\": {");
        sb.append("                          \"id\": ").append(plugin.getId()).append(",");
        sb.append("                          \"name\": \"\",");
        sb.append("                          \"version\": \"\"");
        sb.append("                          }");
        sb.append("}");
        
        return sb.toString();
    }

    protected CustomizedPluginListResponse executeTest(RestTemplateJsonServiceTestHelper<CustomizedPluginListResponse> helper, 
                                                         Plugin gamificationPlugin) throws Exception {
        
        return helper.executeTest("testGetCustomizedGamificationPlugins", 
                                  BASE_RELATIVE_URL + "/getCustomizedGamificationPlugins" + FINAL_PART_RELATIVE_URL,
                                  HttpMethod.POST,
                                  CustomizedPluginListResponse.class, 
                                  this.makeRequest(gamificationPlugin));
    }

    protected void testCustomizedPlugins(CustomizedPluginListResponse response, 
                                         List<CustomizedPlugin> elements) {
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<CustomizedPlugin> responsePlugins = response.getCustomizedPlugins();
            assertNotNull(responsePlugins);
            assertEquals(elements.size(), responsePlugins.size());
            
            for (int i = 0; i < responsePlugins.size(); i++) {
                CustomizedPlugin responsePlugin = responsePlugins.get(i);
                CustomizedPlugin element = elements.get(i);
                
                assertEquals(responsePlugin.getId(), element.getId());
                assertEquals(responsePlugin.getGamificationPlugin().getId(), element.getGamificationPlugin().getId());
                assertEquals(responsePlugin.getName(), element.getName());
                assertEquals(responsePlugin.getGamificationPlugin().getName(), element.getGamificationPlugin().getName());
            }
        }
    }
}