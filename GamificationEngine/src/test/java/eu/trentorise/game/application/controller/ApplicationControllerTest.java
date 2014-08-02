package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.controller.comparator.ApplicationKeyComparator;
import eu.trentorise.game.application.response.ApplicationCollectionResponse;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Luca Piras
 */
public class ApplicationControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_APPLICATIONS_PATH;
    
    /**
     * Test of findApplications method, of class ApplicationController.
     * @throws java.lang.Exception
     */
    @Test
    public void testFindApplications() throws Exception {
        Collection<Application> expectedElements = MockApplicationManager.createInstance().createElements();
        this.executeTestFindApplications((List<Application>) expectedElements);
    }
    
    protected void executeTestFindApplications(List<Application> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ApplicationCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        ApplicationCollectionResponse response = helper.executeTest("ApplicationControllerTest - testFindApplications",
                                                                    BASE_RELATIVE_URL,
                                                                    HttpMethod.GET,
                                                                    ApplicationCollectionResponse.class, 
                                                                    null);
        
        if (null != response) {
            List<Application> responseElements = (List) response.getApplications();
            
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
    
    /**
     * Test of findApplicationById method, of class ApplicationController.
     * @throws java.lang.Exception
     */
    @Test
    public void testFindApplicationById() throws Exception {
        Application expectedElement = MockApplicationManager.createInstance().createViaggiaRovereto();
        this.executeTestFindApplicationById(expectedElement, HttpStatus.OK);
        
        expectedElement.setId(-1);
        this.executeTestFindApplicationById(expectedElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestFindApplicationById(Application expectedElement, 
                                                  HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ApplicationResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        ApplicationResponse response = helper.executeTest("ApplicationControllerTest - testFindApplicationById",
                                                          BASE_RELATIVE_URL + "/" + expectedElement.getId(),
                                                          HttpMethod.GET,
                                                          ApplicationResponse.class, 
                                                          null, 
                                                          expectedStatus);
        
        if (null != response && 0 == expectedStatus.compareTo(HttpStatus.OK)) {
            Application responseElement = response.getApplication();
            
            assertNotNull(responseElement);
            assertEquals(0, (new ApplicationKeyComparator()).compare(expectedElement, responseElement));
        }
    }
}