package eu.trentorise.utils.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;

public class HttpMultipartPostCaller {

    public String call(List<BasicNameValuePair> params, String url, File file) throws Exception {
        String stringResponse = null;
        
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        try {
            List<Part> list = new ArrayList<>();
            for (BasicNameValuePair current : params) {
                StringPart stringPart = new StringPart(current.getName(), 
                                                       current.getValue());
                list.add(stringPart);
            }
            if (null != file) {
                list.add(new FilePart(file.getName(), file));
            }
            
            Part[] stockArray = new Part[list.size()];
            Part[] parts = list.toArray(stockArray);
            
            /*Part[] parts = {
                new StringPart("param_name", "value"),
                filePart
            };*/
            
            post.setRequestEntity(new MultipartRequestEntity(parts, 
                                                             post.getParams()));
            int status = client.executeMethod(post);
            
            if (HttpStatus.SC_OK == status) {
                stringResponse = post.getResponseBodyAsString();
            }
        } finally {
            post.releaseConnection();
        }
        
        return stringResponse;
    }
}