package eu.trentorise.game.servicetest;


import eu.trentorise.utils.rest.RestTemplateJsonCaller;

import static junit.framework.TestCase.assertEquals;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

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
        
        return this.executeTest(testName, relativeUrl, method, responseEntityClass, requestContent, null);
    }
    
    public R executeTest(String testName, String relativeUrl, HttpMethod method, 
                         Class<R> responseEntityClass, String requestContent, 
                         HttpStatus expectedHttpStatus) throws Exception {
        
        if (null == expectedHttpStatus) {
            expectedHttpStatus = HttpStatus.OK;
        }
        
        R responseContent = null;
        
        if (RestTemplateJsonServiceTestHelper.SERVICE_TEST_ACTIVATED && 
            this.active) {
            
            System.out.println(testName);

            RestTemplateJsonCaller<R> caller = new RestTemplateJsonCaller<>();
            try {
                ResponseEntity<R> entity = caller.call(RestTemplateJsonServiceTestHelper.URL_ABSOLUTE + relativeUrl,
                                                       method,
                                                       requestContent, 
                                                       responseEntityClass);

                HttpStatus actualStatusCode = entity.getStatusCode();
                assertEquals(expectedHttpStatus, actualStatusCode);
                //assertTrue(path.startsWith("/gamificationengine/game/services/gameProfile/activateDeactivatePlugin.service"));
                if (0 == actualStatusCode.compareTo(HttpStatus.OK)) {
                    responseContent = entity.getBody();

                    System.out.println ("The result is: " + responseContent.toString());
                    System.out.println ("The Location is " + entity.getHeaders().getLocation());
                }
            } catch(HttpClientErrorException ex) {
                assertEquals(expectedHttpStatus, ex.getStatusCode());
            }
        }
        
        return responseContent;
    }
}