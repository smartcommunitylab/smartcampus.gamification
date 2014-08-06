package eu.trentorise.utils.web;

import java.util.Map;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Luca Piras
 */
public class UrlMaker implements IUrlMaker {
    
    @Override
    public String makeUrl(String urlWithPathVariables, 
                          Map<String, Object> uriVariables) {
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(urlWithPathVariables);
        
        UriComponents uriComponents = builder.buildAndExpand(uriVariables);
        
        return uriComponents.toUriString();
    }
}