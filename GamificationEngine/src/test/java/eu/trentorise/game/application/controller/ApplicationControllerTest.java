package eu.trentorise.game.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.application.model.Application;
import eu.trentorise.game.application.request.ActionRequest;
import eu.trentorise.game.application.response.ActionResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class ApplicationControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_APPLICATION_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public ApplicationControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    
    /**
     * Test of testGetActions method, of class ApplicationController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetActions() throws Exception {
        MockApplicationManager mock = new MockApplicationManager();
        
        
        Application app = mock.createApplication();
        List<Action> expectedElements = mock.createActions();
        this.executeTest(app, expectedElements);
    }
    
    protected void executeTest(Application application,
                               List<Action> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ActionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        ActionRequest request = new ActionRequest();
        Action action = new Action();
        action.setApplication(application);
        request.setAction(action);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        ActionResponse response = helper.executeTest("testGetActions",
                                                     BASE_RELATIVE_URL + "/getActions" + FINAL_PART_RELATIVE_URL,
                                                     ActionResponse.class, 
                                                     jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<Action> responseElements = response.getActions();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                Action responseElement = responseElements.get(i);
                Action expectedElement = expectedElements.get(i);
                
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
}