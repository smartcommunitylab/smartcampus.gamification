package eu.trentorise.game.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.comparator.GameKeyComparator;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.response.GameCollectionResponse;
import eu.trentorise.game.response.NewGameResponse;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;




/**
 *
 * @author Luca Piras
 */
public class GameProfileControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_GAME_PROFILE_GAMES_PATH;
    
    
    public GameProfileControllerTest() {
        super("GameProfileControllerTest");
    }
    
    
    @Test
    public void testCreateGame() throws Exception {
        Game requestElement = MockGameProfileManager.createInstance().createElement();
        requestElement.setId(null);
        this.executeTestCreateGame(requestElement, HttpStatus.CREATED);
    }
    
    protected void executeTestCreateGame(Game requestElement, 
                                         HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest("GameProfileControllerTest - testCreateGame",
                           BASE_RELATIVE_URL,
                           HttpMethod.POST,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
    
    @Test
    public void testReadGames() throws Exception {
        Collection<Game> expectedElements = MockGameProfileManager.createInstance().createElements();
        this.executeTestReadGames((List<Game>) expectedElements);
    }
    
    protected void executeTestReadGames(List<Game> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<GameCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        GameCollectionResponse response = helper.executeTest("GameProfileControllerTest - testReadGames",
                                                             BASE_RELATIVE_URL,
                                                             HttpMethod.GET,
                                                             GameCollectionResponse.class, 
                                                             null);
        
        if (null != response) {
            List<Game> responseElements = (List) response.getGames();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                Game responseElement = responseElements.get(i);
                Game expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getId(), expectedElement.getId());
                assertEquals(responseElement.getName(), 
                             expectedElement.getName());
            }
        }
    }
   
    @Test
    public void testReadGameById() throws Exception {
        Game expectedElement = MockGameProfileManager.createInstance().createElement();
        this.executeTestReadGameById(expectedElement, HttpStatus.OK);
        
        expectedElement.setId(-1);
        this.executeTestReadGameById(expectedElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestReadGameById(Game expectedElement, 
                                                  HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<NewGameResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        NewGameResponse response = helper.executeTest("GameProfileControllerTest - testReadGameById",
                                                      BASE_RELATIVE_URL + "/" + expectedElement.getId(),
                                                      HttpMethod.GET,
                                                      NewGameResponse.class, 
                                                      null, 
                                                      expectedStatus);
        
        if (null != response && 0 == expectedStatus.compareTo(HttpStatus.OK)) {
            Game responseElement = response.getGame();
            
            assertNotNull(responseElement);
            assertEquals(0, (new GameKeyComparator()).compare(expectedElement, responseElement));
        }
    }
    
    @Test
    public void testUpdateGame() throws Exception {
        Game requestElement = MockGameProfileManager.createInstance().createElement();
        requestElement.setName("RoveretoGameModified");
        this.executeTestUpdateGame(requestElement, HttpStatus.NO_CONTENT);
        
        requestElement.setId(-1);
        this.executeTestUpdateGame(requestElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestUpdateGame(Game requestElement, 
                                         HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest("GameProfileControllerTest - testUpdateGame",
                           BASE_RELATIVE_URL + "/" + requestElement.getId(),
                           HttpMethod.PUT,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
    
    @Test
    public void testDeleteGame() throws Exception {
        Game requestElement = MockGameProfileManager.createInstance().createElement();
        this.executeTestDeleteGame(requestElement, HttpStatus.NO_CONTENT);
        
        requestElement.setId(-1);
        this.executeTestDeleteGame(requestElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestDeleteGame(Game requestElement, 
                                                HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        helper.executeTest("GameProfileControllerTest - testUpdateGame",
                           BASE_RELATIVE_URL + "/" + requestElement.getId(),
                           HttpMethod.DELETE,
                           Void.class, 
                           null,
                           expectedStatus);
    }
}