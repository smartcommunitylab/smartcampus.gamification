package eu.trentorise.game.plugin.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Luca Piras
 */
public class PointPluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_POINT_PATH;
    
    @Test
    public void testCreatePointPlugin() throws Exception {
        PointPlugin requestElement = MockPluginManager.createInstance().createGreenLeavesPointPlugin();
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
}