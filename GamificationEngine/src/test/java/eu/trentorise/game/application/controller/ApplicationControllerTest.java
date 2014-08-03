package eu.trentorise.game.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.comparator.ApplicationKeyComparator;
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
    
    @Test
    public void testCreateApplication() throws Exception {
        Application requestElement = MockApplicationManager.createInstance().createViaggiaRovereto();
        requestElement.setId(null);
        this.executeTestCreateApplication(requestElement, HttpStatus.CREATED);
    }
    
    protected void executeTestCreateApplication(Application requestElement, 
                                                HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest("ApplicationControllerTest - testCreateApplication",
                           BASE_RELATIVE_URL,
                           HttpMethod.POST,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
    
    @Test
    public void testReadApplications() throws Exception {
        Collection<Application> expectedElements = MockApplicationManager.createInstance().createElements();
        this.executeTestReadApplications((List<Application>) expectedElements);
    }
    
    protected void executeTestReadApplications(List<Application> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ApplicationCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        ApplicationCollectionResponse response = helper.executeTest("ApplicationControllerTest - testReadApplications",
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
   
    @Test
    public void testReadApplicationById() throws Exception {
        Application expectedElement = MockApplicationManager.createInstance().createViaggiaRovereto();
        this.executeTestReadApplicationById(expectedElement, HttpStatus.OK);
        
        expectedElement.setId(-1);
        this.executeTestReadApplicationById(expectedElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestReadApplicationById(Application expectedElement, 
                                                  HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ApplicationResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        ApplicationResponse response = helper.executeTest("ApplicationControllerTest - testReadApplicationById",
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
    
    @Test
    public void testUpdateApplication() throws Exception {
        Application requestElement = MockApplicationManager.createInstance().createViaggiaRovereto();
        requestElement.setName("VIAGGIAROVERETO22222");
        this.executeTestUpdateApplication(requestElement, HttpStatus.NO_CONTENT);
        
        requestElement.setId(-1);
        this.executeTestUpdateApplication(requestElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestUpdateApplication(Application requestElement, 
                                                HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest("ApplicationControllerTest - testUpdateApplication",
                           BASE_RELATIVE_URL + "/" + requestElement.getId(),
                           HttpMethod.PUT,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
}