package eu.trentorise.game.servicetest;

import eu.trentorise.utils.http.HttpPostCaller;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Luca Piras
 */
//TODO: to be tested
public class HttpPostCallerServiceTestHelper {
    
    //TODO: improve this one, maybe creating a common configuration interface
    //where to centralize the ACTIVE parameter
    public static final boolean ACTIVE = RestTemplateJsonServiceTestHelper.ACTIVE;
    public static final String URL_ABSOLUTE = RestTemplateJsonServiceTestHelper.URL_ABSOLUTE;

    protected boolean active;
    
    public HttpPostCallerServiceTestHelper(boolean active) {
        this.active = active;
    }
    
    public String executeTest(String testName, String relativeUrl,
                         List<BasicNameValuePair> params) throws Exception {
        
        String response = null;
        
        if (this.ACTIVE && this.active) {
            System.out.println(testName);

            HttpPostCaller caller = new HttpPostCaller();
            response = caller.call(params, this.URL_ABSOLUTE + relativeUrl);

            //System.out.println ("The result is: " + response);
        }
        
        return response;
    }
}