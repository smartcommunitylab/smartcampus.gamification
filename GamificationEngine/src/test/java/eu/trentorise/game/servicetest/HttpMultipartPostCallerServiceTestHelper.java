package eu.trentorise.game.servicetest;

import eu.trentorise.utils.http.HttpMultipartPostCaller;
import java.io.File;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Luca Piras
 */
public class HttpMultipartPostCallerServiceTestHelper {
    
    public static final boolean ACTIVE = RestTemplateJsonServiceTestHelper.ACTIVE;
    public static final String URL_ABSOLUTE = RestTemplateJsonServiceTestHelper.URL_ABSOLUTE;

    protected boolean active;
    
    public HttpMultipartPostCallerServiceTestHelper(boolean active) {
        this.active = active;
    }
    
    public String executeTest(String testName, String relativeUrl,
                              List<BasicNameValuePair> params, 
                              File file) throws Exception {
        
        String response = null;
        
        if (this.ACTIVE && this.active) {
            System.out.println(testName);

            HttpMultipartPostCaller caller = new HttpMultipartPostCaller();
            response = caller.call(params, this.URL_ABSOLUTE + relativeUrl,
                                   file);

            //System.out.println ("The result is: " + response);
        }
        
        return response;
    }
}