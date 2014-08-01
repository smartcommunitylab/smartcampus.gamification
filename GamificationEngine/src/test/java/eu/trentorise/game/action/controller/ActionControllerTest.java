package eu.trentorise.game.action.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.model.Param;
import eu.trentorise.game.action.request.ActionRequest;
import eu.trentorise.game.action.request.ExternalActionRequest;
import eu.trentorise.game.action.response.ExternalActionResponse;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.action.service.MockActionManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
public class ActionControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_ACTION_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    /**
     * Test of testGetExternalActions method, of class ActionController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetExternalActions() throws Exception {
        MockActionManager mock = MockActionManager.createInstance();
        
        Application app = mock.getManager().createViaggiaRovereto();
        List<ExternalAction> expectedElements = mock.createActions();
        this.executeTestGetExternalActions(app, expectedElements);
    }
    
    protected void executeTestGetExternalActions(Application application,
                               List<ExternalAction> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ExternalActionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        ExternalActionRequest request = new ExternalActionRequest();
        ExternalAction action = new ExternalAction();
        action.setApplication(application);
        request.setAction(action);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        ExternalActionResponse response = helper.executeTest("testGetExternalActions",
                                                     BASE_RELATIVE_URL + "/getExternalActions" + FINAL_PART_RELATIVE_URL,
                                                     HttpMethod.POST,
                                                     ExternalActionResponse.class, 
                                                     jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<ExternalAction> responseElements = response.getActions();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                ExternalAction responseElement = responseElements.get(i);
                ExternalAction expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getId(), expectedElement.getId());
                assertEquals(responseElement.getApplication().getId(), 
                             expectedElement.getApplication().getId());
                assertEquals(responseElement.getName(), 
                             expectedElement.getName());
                assertEquals(responseElement.getDescription(), 
                             expectedElement.getDescription());
            }
        }
    }
    
    /**
     * Test of testGetActionParams method, of class ActionController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetActionParams() throws Exception {
        MockActionManager mock = MockActionManager.createInstance();
        
        Action action = mock.createExternalAction();
        List<Param> expectedElements = mock.createElements();
        this.executeTestGetActionParams(action, expectedElements);
    }
    
    protected void executeTestGetActionParams(Action action,
                               List<Param> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ParamResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        ActionRequest request = new ActionRequest();
        request.setAction(action);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        ParamResponse response = helper.executeTest("testGetActionParams",
                                                    BASE_RELATIVE_URL + "/getActionParams" + FINAL_PART_RELATIVE_URL,
                                                    HttpMethod.POST,
                                                    ParamResponse.class, 
                                                    jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<Param> responseElements = response.getParams();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                Param responseElement = responseElements.get(i);
                Param expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getName(), expectedElement.getName());
                assertEquals(responseElement.getAction().getId(), 
                             expectedElement.getAction().getId());
                assertEquals(responseElement.getCompositeParam(), 
                             expectedElement.getCompositeParam());
            }
        }
    }
}