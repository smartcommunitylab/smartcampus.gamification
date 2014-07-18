package eu.trentorise.utils.http;

import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

//TODO: TO BE TESTED
public class HttpPostCaller {

    public String call(List<BasicNameValuePair> params, String url) throws Exception {
        String stringResponse = null;
        
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);

            /*List<BasicNameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("aParamName", "aParamValue"));
            nameValuePairs.add(new BasicNameValuePair("bParamName", "bParamValue"));*/

            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(post);

            stringResponse = EntityUtils.toString(response.getEntity());
        } finally {
            client.close();
        }
        
        return stringResponse;
    }
}