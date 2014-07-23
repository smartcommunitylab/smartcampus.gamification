package eu.trentorise.game.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.application.model.ExternalAction;
import eu.trentorise.game.application.model.Param;
import eu.trentorise.game.application.request.ExternalActionRequest;
import eu.trentorise.game.application.response.ParamResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.application.service.MockGamifiableActionManager;
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
public class GamifiableActionControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_APPLICATION_ACTION_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public GamifiableActionControllerTest() {
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
     * Test of testGetParams method, of class GamifiableActionController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetParams() throws Exception {
        MockApplicationManager mock = new MockApplicationManager();
        MockGamifiableActionManager gamifiableActionMock = new MockGamifiableActionManager();
        gamifiableActionMock.setManager(mock);
        
        
        ExternalAction action = gamifiableActionMock.createAction();
        List<Param> expectedElements = gamifiableActionMock.createElements();
        this.executeTest(action, expectedElements);
    }
    
    protected void executeTest(ExternalAction action,
                               List<Param> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ParamResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        ExternalActionRequest request = new ExternalActionRequest();
        request.setAction(action);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        ParamResponse response = helper.executeTest("testGetParams",
                                                    BASE_RELATIVE_URL + "/getParams" + FINAL_PART_RELATIVE_URL,
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