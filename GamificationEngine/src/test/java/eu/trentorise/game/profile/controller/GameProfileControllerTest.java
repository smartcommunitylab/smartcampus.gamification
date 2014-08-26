package eu.trentorise.game.profile.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.response.GameCollectionResponse;
import eu.trentorise.game.response.NewGameResponse;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.List;
import org.junit.Test;


/**
 *
 * @author Luca Piras
 */
public class GameProfileControllerTest extends AbstractRestCrudTest<Game, 
                                                                    Object,
                                                                    Game,
                                                                    GameCollectionResponse,
                                                                    NewGameResponse> {
    
    protected static final MockGameProfileManager mockGameProfileManager = MockGameProfileManager.createInstance();
    
    
    public GameProfileControllerTest() {
        super("GameProfileControllerTest", 
              IGameConstants.SERVICE_GAME_PROFILE_GAMES_PATH,
              mockGameProfileManager,
              mockGameProfileManager.getComparator());
    }
    
    
    @Test
    public void testGame() throws Exception {
        super.testCreateElement("testCreateGame", null, null);
        
        super.testReadCollection("testReadGames", null, 
                                 GameCollectionResponse.class, null);
        
        super.testReadElementById("testReadGameById", null, 
                                  NewGameResponse.class, null);
        
        super.testUpdateElement("testUpdateGame", null, null);
        
        //super.testDeleteElement("testDeleteGame", null, null);
    }
    
    //CREATE
    /*@Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateGame", null, null);
    }*/
    
    @Override
    protected Game manageElementToCreate(Game element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    /*@Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadGames", null, 
                                 GameCollectionResponse.class, null);
    }*/
    
    @Override
    protected List<Game> retrieveCollection(GameCollectionResponse response) {
        return (List<Game>) response.getGames();
    }
    
    
    //READ SINGLE ELEMENT
    /*@Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadGameById", null, 
                                  GameResponse.class, null);
    }*/

    @Override
    protected Game manageNegativeElementToReadById(Game element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected Game retrieveSingleElement(NewGameResponse response) {
        return response.getGame();
    }
    
    
    //UPDATE
    /*@Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateGame", null, null);
    }*/

    @Override
    protected Game managePositiveElementToUpdate(Game element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected Game manageNegativeElementToUpdate(Game element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    /*@Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteGame", null, null);
    }*/
    
    @Override
    protected Game manageNegativeElementToDelete(Game element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(Game element) {
        return element.getId().toString();
    }

    
    protected Game setNegativeId(Game element) {
        element.setId(-1);
        
        return element;
    }
}