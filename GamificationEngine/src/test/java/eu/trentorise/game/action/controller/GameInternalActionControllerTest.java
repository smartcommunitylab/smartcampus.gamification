package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.model.GameInternalAction;
import eu.trentorise.game.action.response.GameInternalActionCollectionResponse;
import eu.trentorise.game.action.response.GameInternalActionResponse;
import eu.trentorise.game.action.service.MockGameInternalActionManager;
import eu.trentorise.game.controller.IGameConstants;
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
public class GameInternalActionControllerTest extends AbstractRestCrudTest<GameInternalAction, 
                                                                           GameInternalAction,
                                                                           GameInternalAction,
                                                                           GameInternalActionCollectionResponse,
                                                                           GameInternalActionResponse> {
    
    protected static final MockGameInternalActionManager mockGameInternalActionManager = MockGameInternalActionManager.createInstance();
    
    
    public GameInternalActionControllerTest() {
        super("GameInternalActionControllerTest", 
              IGameConstants.SERVICE_GAME_INTERNALACTIONS_PATH,
              mockGameInternalActionManager,
              mockGameInternalActionManager.getComparator());
    }
    
    
    //CREATE
    /*@Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateGameInternalAction", null, 
                                makeBaseRelativeUrlExpanded(mockGameInternalActionManager.createViaggiaRovereto()));
    }*/
    
    @Override
    protected GameInternalAction manageElementToCreate(GameInternalAction element) {
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadGameInternalActions", 
                                 null, 
                                 GameInternalActionCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(mockGameInternalActionManager.createElement(null)));
    }
    
    @Override
    protected List<GameInternalAction> retrieveCollection(GameInternalActionCollectionResponse response) {
        return (List<GameInternalAction>) response.getGameInternalActions();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadGameInternalActionById", null, 
                                  GameInternalActionResponse.class,
                                  makeBaseRelativeUrlExpanded(mockGameInternalActionManager.createElement(null)));
    }

    @Override
    protected GameInternalAction manageNegativeElementToReadById(GameInternalAction element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected GameInternalAction retrieveSingleElement(GameInternalActionResponse response) {
        return response.getGameInternalAction();
    }
    
    
    //UPDATE
    /*@Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateGameInternalAction", null,
                                makeBaseRelativeUrlExpanded(mockGameInternalActionManager.createViaggiaRovereto()));
    }*/

    @Override
    protected GameInternalAction managePositiveElementToUpdate(GameInternalAction element) {
        return element;
    }

    @Override
    protected GameInternalAction manageNegativeElementToUpdate(GameInternalAction element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    /*@Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteGameInternalAction", null,
                                makeBaseRelativeUrlExpanded(mockGameInternalActionManager.createViaggiaRovereto()));
    }*/
    
    @Override
    protected GameInternalAction manageNegativeElementToDelete(GameInternalAction element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(GameInternalAction element) {
        return element.getInternalAction().getId().toString();
    }

    
    protected GameInternalAction setNegativeId(GameInternalAction element) {
        element.getInternalAction().setId(-1);
        
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(GameInternalAction element) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, 
                         element.getGame().getId());
        
        IUrlMaker urlMaker = new UrlMaker();
        return urlMaker.makeUrl(this.baseRelativeUrl, uriVariables);
    }
}