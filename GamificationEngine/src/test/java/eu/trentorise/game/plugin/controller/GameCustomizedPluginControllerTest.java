package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.CustomizedPluginCollectionResponse;
import eu.trentorise.game.plugin.service.MockGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import eu.trentorise.utils.web.IUrlMaker;
import eu.trentorise.utils.web.UrlMaker;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;


/**
 *
 * @author Luca Piras
 */
public class GameCustomizedPluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;

    
    @Test
    public void testReadCustomizedPlugins() throws Exception {
        MockGameCustomizedPluginManager mockCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        MockPluginManager mockPluginManager = MockPluginManager.createInstance();
        
        Plugin plugin = mockPluginManager.createPointsPlugin();
        IGameCustomizedPluginCollectionContainer container = mockCustomizedPluginManager.createContainer(135, plugin);
        Collection<CustomizedPlugin> expectedElements = mockPluginManager.createCustomizedPlugins(container);
        this.executeTestReadCustomizedPlugins(container.getGameCustomizedPlugin(),
                                              (List<CustomizedPlugin>) expectedElements);
        
        plugin = mockPluginManager.createBadgeCollectionPlugin();
        container = mockCustomizedPluginManager.createContainer(135, plugin);
        expectedElements = mockPluginManager.createCustomizedPlugins(container);
        
        this.executeTestReadCustomizedPlugins(container.getGameCustomizedPlugin(),
                                              (List<CustomizedPlugin>) expectedElements);
        
        plugin = mockPluginManager.createLeadearboardPointPlugin();
        container = mockCustomizedPluginManager.createContainer(135, plugin);
        expectedElements = mockPluginManager.createCustomizedPlugins(container);
        
        this.executeTestReadCustomizedPlugins(container.getGameCustomizedPlugin(),
                                              (List<CustomizedPlugin>) expectedElements);
    }
    
    protected void executeTestReadCustomizedPlugins(GameCustomizedPlugin gcp, 
                                                    List<CustomizedPlugin> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<CustomizedPluginCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        IUrlMaker urlMaker = new UrlMaker();
        
        String relativeUrl = urlMaker.makeUrl(BASE_RELATIVE_URL, 
                                              this.makeCollectionUriVariables(gcp));
        
        
        
        
        CustomizedPluginCollectionResponse response = helper.executeTest("PluginControllerTest - testReadCustomizedPlugins",
                                                                         relativeUrl,
                                                                         HttpMethod.GET,
                                                                         CustomizedPluginCollectionResponse.class, 
                                                                         null);
        
        if (null != response) {
            List<CustomizedPlugin> responseElements = (List) response.getCustomizedPlugins();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                CustomizedPlugin responseElement = responseElements.get(i);
                CustomizedPlugin expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getId(), expectedElement.getId());
                assertEquals(responseElement.getGamificationPlugin().getId(), 
                             expectedElement.getGamificationPlugin().getId());
            }
        }
    }
    
    
    protected Map<String, Object> makeCollectionUriVariables(GameCustomizedPlugin element) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, 
                         element.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, 
                         element.getCustomizedPlugin().getGamificationPlugin().getId());
        
        return uriVariables;
    }
    
    protected Map<String, Object> makeUriVariables(GameCustomizedPlugin element) {
        Map<String, Object> uriVariables = this.makeCollectionUriVariables(element);
        
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, 
                         element.getCustomizedPlugin().getId());
        
        return uriVariables;
    }
}