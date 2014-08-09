package eu.trentorise.game.plugin.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.point.response.PointPluginCollectionResponse;
import eu.trentorise.game.plugin.point.response.PointPluginResponse;
import eu.trentorise.game.plugin.point.service.MockPointPluginManager;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.Collection;
import java.util.Comparator;
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
public class PointPluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_POINT_PATH;
    
    protected MockPointPluginManager manager;
    protected Comparator<CustomizedPlugin> comparator;

    public PointPluginControllerTest() {
        this.manager = MockPointPluginManager.createInstance();
        this.comparator = this.manager.getComparator();
    }
    
    
    @Test
    public void testCreatePointPlugin() throws Exception {
        PointPlugin requestElement = manager.createElement();
        requestElement.setId(null);
        this.executeTestCreatePointPlugin(requestElement, HttpStatus.CREATED);
    }
    
    protected void executeTestCreatePointPlugin(PointPlugin requestElement, 
                                                HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest("PointPluginControllerTest - testCreatePointPlugin",
                           BASE_RELATIVE_URL,
                           HttpMethod.POST,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
    
    @Test
    public void testReadPointPlugins() throws Exception {
        Collection<PointPlugin> expectedElements = manager.createElements();
        this.executeTestReadPointPlugins((List<PointPlugin>) expectedElements);
    }
    
    protected void executeTestReadPointPlugins(List<PointPlugin> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<PointPluginCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        PointPluginCollectionResponse response = helper.executeTest("PointPluginControllerTest - testReadPointPlugins",
                                                                    BASE_RELATIVE_URL,
                                                                    HttpMethod.GET,
                                                                    PointPluginCollectionResponse.class, 
                                                                    null);
        
        if (null != response) {
            List<PointPlugin> responseElements = (List) response.getPointPlugins();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                PointPlugin responseElement = responseElements.get(i);
                PointPlugin expectedElement = expectedElements.get(i);
                
                assertEquals(0, this.comparator.compare(responseElement, 
                                                        expectedElement));
            }
        }
    }
    
    @Test
    public void testReadPointPluginById() throws Exception {
        PointPlugin expectedElement = manager.createElement();
        this.executeTestReadPointPluginById(expectedElement, HttpStatus.OK);
        
        expectedElement.setId(-1);
        this.executeTestReadPointPluginById(expectedElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestReadPointPluginById(PointPlugin expectedElement, 
                                                  HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<PointPluginResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        PointPluginResponse response = helper.executeTest("PointPluginControllerTest - testReadPointPluginById",
                                                          BASE_RELATIVE_URL + "/" + expectedElement.getId(),
                                                          HttpMethod.GET,
                                                          PointPluginResponse.class, 
                                                          null, 
                                                          expectedStatus);
        
        if (null != response && 0 == expectedStatus.compareTo(HttpStatus.OK)) {
            PointPlugin responseElement = response.getPointPlugin();
            
            assertNotNull(responseElement);
            assertEquals(0, this.comparator.compare(expectedElement, responseElement));
        }
    }
    
    @Test
    public void testUpdatePointPlugin() throws Exception {
        PointPlugin requestElement = manager.createElement();
        requestElement.setName(requestElement.getName() + "Modified");
        this.executeTestUpdatePointPlugin(requestElement, HttpStatus.NO_CONTENT);
        
        requestElement.setId(-1);
        this.executeTestUpdatePointPlugin(requestElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestUpdatePointPlugin(PointPlugin requestElement, 
                                                HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest("PointPluginControllerTest - testUpdatePointPlugin",
                           BASE_RELATIVE_URL + "/" + requestElement.getId(),
                           HttpMethod.PUT,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
    
    @Test
    public void testDeletePointPlugin() throws Exception {
        PointPlugin requestElement = manager.createElement();
        this.executeTestDeletePointPlugin(requestElement, HttpStatus.NO_CONTENT);
        
        requestElement.setId(-1);
        this.executeTestDeletePointPlugin(requestElement, HttpStatus.NOT_FOUND);
    }
    
    protected void executeTestDeletePointPlugin(PointPlugin requestElement, 
                                                HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        helper.executeTest("PointPluginControllerTest - testDeletePointPlugin",
                           BASE_RELATIVE_URL + "/" + requestElement.getId(),
                           HttpMethod.DELETE,
                           Void.class, 
                           null,
                           expectedStatus);
    }
}