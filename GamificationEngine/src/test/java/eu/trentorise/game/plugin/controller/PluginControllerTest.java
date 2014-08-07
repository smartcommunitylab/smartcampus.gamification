package eu.trentorise.game.plugin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.comparator.PluginKeyComparator;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.request.CustomizedPluginActivationDeactivationRequest;
import eu.trentorise.game.plugin.response.PluginCollectionResponse;
import eu.trentorise.game.plugin.response.PluginResponse;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
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
    
    @Test
    public void testActivateDeactivateCustomizedGamificationPlugin() throws Exception {
        RestTemplateJsonServiceTestHelper<GameResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        MockPluginManager mock = new MockPluginManager();
        ObjectMapper mapper = new ObjectMapper();
        
        CustomizedPluginActivationDeactivationRequest request = new CustomizedPluginActivationDeactivationRequest();
        
        Game game = new Game();
        game.setId(MockGameProfileManager.MOCK_GAME_ID);
        
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
}