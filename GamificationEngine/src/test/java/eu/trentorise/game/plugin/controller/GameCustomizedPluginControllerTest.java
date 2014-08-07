package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.comparator.CustomizedPluginKeyComparator;
import eu.trentorise.game.plugin.comparator.PluginKeyComparator;
import eu.trentorise.game.plugin.container.GameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.GameCustomizedPluginContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.CustomizedPluginCollectionResponse;
import eu.trentorise.game.plugin.response.CustomizedPluginResponse;
import eu.trentorise.game.plugin.service.MockGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
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
import org.springframework.http.HttpStatus;


/**
 *
 * @author Luca Piras
 */
public class GameCustomizedPluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_PATH;
    protected final static String BASE_RELATIVE_URL_SINGLE_PATH = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;

    
    @Test
    public void testReadGameCustomizedPlugins() throws Exception {
        MockGameCustomizedPluginManager mockCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        MockPluginManager mockPluginManager = MockPluginManager.createInstance();
        
        IGameCustomizedPluginCollectionContainer container = new GameCustomizedPluginCollectionContainer();
        
        Plugin plugin = mockPluginManager.createPointsPlugin();
        container.setGameCustomizedPlugin(mockCustomizedPluginManager.createContainerContent(MockGameProfileManager.MOCK_GAME_ID, plugin, null));
        Collection<CustomizedPlugin> expectedElements = mockPluginManager.createCustomizedPlugins(container);
        this.executeTestReadGameCustomizedPlugins(container.getGameCustomizedPlugin(),
                                                  (List<CustomizedPlugin>) expectedElements);
        
        plugin = mockPluginManager.createBadgeCollectionPlugin();
        container.setGameCustomizedPlugin(mockCustomizedPluginManager.createContainerContent(MockGameProfileManager.MOCK_GAME_ID, plugin, null));
        expectedElements = mockPluginManager.createCustomizedPlugins(container);
        
        this.executeTestReadGameCustomizedPlugins(container.getGameCustomizedPlugin(),
                                                  (List<CustomizedPlugin>) expectedElements);
        
        plugin = mockPluginManager.createLeadearboardPointPlugin();
        container.setGameCustomizedPlugin(mockCustomizedPluginManager.createContainerContent(MockGameProfileManager.MOCK_GAME_ID, plugin, null));
        expectedElements = mockPluginManager.createCustomizedPlugins(container);
        
        this.executeTestReadGameCustomizedPlugins(container.getGameCustomizedPlugin(),
                                                  (List<CustomizedPlugin>) expectedElements);
    }
    
    protected void executeTestReadGameCustomizedPlugins(GameCustomizedPlugin gcp, 
                                                        List<CustomizedPlugin> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<CustomizedPluginCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        IUrlMaker urlMaker = new UrlMaker();
        
        String relativeUrl = urlMaker.makeUrl(BASE_RELATIVE_URL, 
                                              this.makeCollectionUriVariables(gcp));
        
        CustomizedPluginCollectionResponse response = helper.executeTest("PluginControllerTest - testReadGameCustomizedPlugins",
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
                assertEquals(responseElement.getPlugin().getId(), 
                             expectedElement.getPlugin().getId());
            }
        }
    }
    
    @Test
    public void testReadGameCustomizedPluginById() throws Exception {
        MockGameCustomizedPluginManager mockCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        
        IGameCustomizedPluginContainer container = new GameCustomizedPluginContainer();
        
        CustomizedPlugin expectedElement = MockPluginManager.createInstance().createGreenLeavesPointPlugin();
        GameCustomizedPlugin containerContent = mockCustomizedPluginManager.createContainerContent(MockGameProfileManager.MOCK_GAME_ID, 
                                                                                                   expectedElement.getPlugin(),
                                                                                                   expectedElement.getId());
        container.setGameCustomizedPlugin(containerContent);
        GameCustomizedPlugin gameCustomizedPlugin = container.getGameCustomizedPlugin();
        
        
        this.executeTestReadGameCustomizedPluginById(gameCustomizedPlugin, expectedElement, HttpStatus.OK);
        
        gameCustomizedPlugin.getCustomizedPlugin().setId(-1);
        this.executeTestReadGameCustomizedPluginById(gameCustomizedPlugin, expectedElement, HttpStatus.NOT_FOUND);
        
        gameCustomizedPlugin.getCustomizedPlugin().setId(0);
        this.executeTestReadGameCustomizedPluginById(gameCustomizedPlugin, expectedElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestReadGameCustomizedPluginById(GameCustomizedPlugin gcp,
                                                           CustomizedPlugin expectedElement, 
                                                           HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<CustomizedPluginResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        IUrlMaker urlMaker = new UrlMaker();
        
        String relativeUrl = urlMaker.makeUrl(BASE_RELATIVE_URL_SINGLE_PATH, 
                                              this.makeUriVariables(gcp));
        
        CustomizedPluginResponse response = helper.executeTest("PluginControllerTest - readGameCustomizedPluginById",
                                                               relativeUrl,
                                                               HttpMethod.GET,
                                                               CustomizedPluginResponse.class,
                                                               null, 
                                                               expectedStatus);
        
        if (null != response && 0 == expectedStatus.compareTo(HttpStatus.OK)) {
            CustomizedPlugin responseElement = response.getCustomizedPlugin();
            
            assertNotNull(responseElement);
            
            CustomizedPluginKeyComparator comparator = new CustomizedPluginKeyComparator();
            PluginKeyComparator pluginKeyComparator = new PluginKeyComparator();
            comparator.setGamificationPluginComparator(pluginKeyComparator);
            
            assertEquals(0, comparator.compare(expectedElement, responseElement));
        }
    }
    
    
    protected Map<String, Object> makeCollectionUriVariables(GameCustomizedPlugin element) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, 
                         element.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, 
                         element.getCustomizedPlugin().getPlugin().getId());
        
        return uriVariables;
    }
    
    protected Map<String, Object> makeUriVariables(GameCustomizedPlugin element) {
        Map<String, Object> uriVariables = this.makeCollectionUriVariables(element);
        
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, 
                         element.getCustomizedPlugin().getId());
        
        return uriVariables;
    }
}