package eu.trentorise.game.servicetest;


import eu.trentorise.utils.rest.RestTemplateJsonCaller;
import org.springframework.http.*;

import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author Luca Piras
 */
public class RestTemplateJsonServiceTestHelper<R> {
    
    public static final boolean ACTIVE = true;
    public static final String URL_ABSOLUTE = "http://localhost:8080/GamificationEngine";
    
    public R executeTest(String testName, String relativeUrl, 
                         Class<R> responseEntityClass, String requestContent) throws Exception {
        
        R responseContent = null;
        
        if (this.ACTIVE) {
            System.out.println(testName);

            RestTemplateJsonCaller<R> caller = new RestTemplateJsonCaller<>();
            ResponseEntity<R> entity = caller.call(URL_ABSOLUTE + relativeUrl,
                                                   requestContent, 
                                                   responseEntityClass);

            assertEquals(HttpStatus.OK, entity.getStatusCode());
            //assertTrue(path.startsWith("/GamificationEngine/game/services/gameProfile/activateDeactivatePlugin.service"));
            responseContent = entity.getBody();

            System.out.println ("The result is: " + responseContent.toString());
            System.out.println ("The Location is " + entity.getHeaders().getLocation());
        }
        
        return responseContent;
    }
}