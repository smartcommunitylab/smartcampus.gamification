package eu.trentorise.game.aaa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.aaa.model.User;
import eu.trentorise.game.aaa.request.GameAuthenticationRequest;
import eu.trentorise.game.aaa.service.MockGameAuthenticationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Luca Piras
 */
public class GameAuthenticationControllerTest extends SkipServiceTestHelper {

    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_GAME_AAA_PATH;
    
    @Test
    public void testAuthenticate() throws Exception {
        User requestElement = MockGameAuthenticationManager.createInstance().createUser();
        this.executeTestAuthenticate(requestElement, HttpStatus.NO_CONTENT);
        
        requestElement.setPassword("wrongPassword");
        this.executeTestAuthenticate(requestElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestAuthenticate(User requestElement, 
                                           HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        GameAuthenticationRequest request = new GameAuthenticationRequest();
        request.setUser(requestElement);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        helper.executeTest("GameAuthenticationControllerTest - testAuthenticate",
                           BASE_RELATIVE_URL + "/login",
                           HttpMethod.POST,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
}