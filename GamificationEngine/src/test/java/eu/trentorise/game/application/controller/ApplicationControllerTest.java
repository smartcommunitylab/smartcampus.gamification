package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
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
public class ApplicationControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_APPLICATIONS_PATH;
    
    /**
     * Test of get method, of class ApplicationController.
     */
    @Test
    public void testGet() throws Exception {
        List<Application> expectedElements = MockApplicationManager.createInstance().createElements();
        this.executeTestGet(expectedElements);
    }
    
    protected void executeTestGet(List<Application> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ApplicationResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        ApplicationResponse response = helper.executeTest("ApplicationControllerTest - testGet",
                                                          BASE_RELATIVE_URL,
                                                          HttpMethod.GET,
                                                          ApplicationResponse.class, 
                                                          null);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<Application> responseElements = response.getApplications();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                Application responseElement = responseElements.get(i);
                Application expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getId(), expectedElement.getId());
                assertEquals(responseElement.getName(), 
                             expectedElement.getName());
            }
        }
    }
}