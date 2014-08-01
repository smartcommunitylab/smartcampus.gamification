package eu.trentorise.utils.rest;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 *
 * @author Luca Piras
 * @param <R> class of the content of the response
 */
public class RestTemplateJsonCaller<R> {
    
    public ResponseEntity<R> call(String url, HttpMethod method, 
                                  String requestContent, 
                                  Class<R> responseEntityClass) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        RestTemplate template = new RestTemplate();

        HttpEntity<String> requestEntity = new HttpEntity<>(requestContent,
                                                            headers);
        
        ResponseEntity<R> responseEntity;
        if (0 == HttpMethod.POST.compareTo(method)) {
            responseEntity = template.postForEntity(url, requestEntity, responseEntityClass);
        } else {
            responseEntity = template.getForEntity(url, responseEntityClass);
        }
        
        return responseEntity;
    }
}