package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.response.GameCustomizedPluginCollectionResponse;
import eu.trentorise.game.plugin.response.GameCustomizedPluginResponse;
import eu.trentorise.game.plugin.service.MockGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import eu.trentorise.utils.web.IUrlMaker;
import eu.trentorise.utils.web.UrlMaker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Test;


/**
 *
 * @author Luca Piras
 */
public class GameCustomizedPluginControllerTest extends AbstractRestCrudTest<GameCustomizedPlugin,
                                                                             GameCustomizedPlugin, 
                                                                             GameCustomizedPlugin,
                                                                             GameCustomizedPluginCollectionResponse,
                                                                             GameCustomizedPluginResponse> {
    
    protected static final MockGameCustomizedPluginManager mockGameCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
    protected static final MockPluginManager mockPluginManager = MockPluginManager.createInstance();

    
    public GameCustomizedPluginControllerTest() {
        super("GameCustomizedPluginControllerTest", 
              IGameConstants.SERVICE_GAMECUSTOMIZEDPLUGINS_PATH,
              mockGameCustomizedPluginManager,
              mockGameCustomizedPluginManager.getComparator());
    }
    
    
    //CREATE
    @Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateGameCustomizedPlugin", null, 
                                makeBaseRelativeUrlExpanded(mockGameCustomizedPluginManager.createElement(null)));
    }
    
    @Override
    protected GameCustomizedPlugin manageElementToCreate(GameCustomizedPlugin element) {
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        GameCustomizedPlugin element = mockGameCustomizedPluginManager.createElement(null);
        CustomizedPlugin cp = element.getCustomizedPlugin();
        //in this way we receive all customizedPlugins of a specified plugin
        //associated with the specified game
        cp.setId(null);
        cp.setPlugin(mockPluginManager.createPointsPlugin());
        super.testReadCollection("testReadGameCustomizedPlugins", 
                                 element, 
                                 GameCustomizedPluginCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(element));
        
        cp.setPlugin(mockPluginManager.createBadgeCollectionPlugin());
        super.testReadCollection("testReadGameCustomizedPlugins", 
                                 element, 
                                 GameCustomizedPluginCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(element));
        
        cp.setPlugin(mockPluginManager.createLeadearboardPointPlugin());
        super.testReadCollection("testReadGameCustomizedPlugins", 
                                 element, 
                                 GameCustomizedPluginCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(element));
    }
    
    @Override
    protected List<GameCustomizedPlugin> retrieveCollection(GameCustomizedPluginCollectionResponse response) {
        return (List<GameCustomizedPlugin>) response.getGameCustomizedPlugins();
    }
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadGameCustomizedPluginById", null, 
                                  GameCustomizedPluginResponse.class,
                                  makeBaseRelativeUrlExpanded(mockGameCustomizedPluginManager.createElement(null)));
    }

    @Override
    protected GameCustomizedPlugin manageNegativeElementToReadById(GameCustomizedPlugin element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected GameCustomizedPlugin retrieveSingleElement(GameCustomizedPluginResponse response) {
        return response.getGameCustomizedPlugin();
    }
    
    
    //UPDATE
    @Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateGameCustomizedPlugin", null,
                                makeBaseRelativeUrlExpanded(mockGameCustomizedPluginManager.createElement(null)));
    }

    @Override
    protected GameCustomizedPlugin managePositiveElementToUpdate(GameCustomizedPlugin element) {
        element.setActivated(false);
        
        return element;
    }

    @Override
    protected GameCustomizedPlugin manageNegativeElementToUpdate(GameCustomizedPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteGameCustomizedPlugin", null,
                                makeBaseRelativeUrlExpanded(mockGameCustomizedPluginManager.createElement(null)));
    }
    
    @Override
    protected GameCustomizedPlugin manageNegativeElementToDelete(GameCustomizedPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(GameCustomizedPlugin element) {
        return element.getCustomizedPlugin().getId().toString();
    }

    
    protected GameCustomizedPlugin setNegativeId(GameCustomizedPlugin element) {
        element.getCustomizedPlugin().setId(-1);
        
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(GameCustomizedPlugin element) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, 
                         element.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, 
                         element.getCustomizedPlugin().getPlugin().getId());
        
        IUrlMaker urlMaker = new UrlMaker();
        return urlMaker.makeUrl(this.baseRelativeUrl, uriVariables);
    }
}