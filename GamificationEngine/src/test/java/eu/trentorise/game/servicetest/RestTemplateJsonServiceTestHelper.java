package eu.trentorise.game.servicetest;


import eu.trentorise.utils.rest.RestTemplateJsonCaller;
import org.springframework.http.*;

import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author Luca Piras
 * @param <R>
 */
public class RestTemplateJsonServiceTestHelper<R> implements IServiceTestConfiguration {
    
    protected boolean active;
    
    public RestTemplateJsonServiceTestHelper(boolean active) {
        this.active = active;
    }
    
    public R executeTest(String testName, String relativeUrl, HttpMethod method, 
                         Class<R> responseEntityClass, String requestContent) throws Exception {
        
        R responseContent = null;
        
        if (RestTemplateJsonServiceTestHelper.SERVICE_TEST_ACTIVATED && 
            this.active) {
            
            System.out.println(testName);

            RestTemplateJsonCaller<R> caller = new RestTemplateJsonCaller<>();
            ResponseEntity<R> entity = caller.call(RestTemplateJsonServiceTestHelper.URL_ABSOLUTE + relativeUrl,
                                                   method,
                                                   requestContent, 
                                                   responseEntityClass);

            assertEquals(HttpStatus.OK, entity.getStatusCode());
            //assertTrue(path.startsWith("/gamificationengine/game/services/gameProfile/activateDeactivatePlugin.service"));
            responseContent = entity.getBody();

            System.out.println ("The result is: " + responseContent.toString());
            System.out.println ("The Location is " + entity.getHeaders().getLocation());
        }
        
        return responseContent;
    }
}