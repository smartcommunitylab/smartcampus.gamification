package eu.trentorise.utils.rest;

import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Luca Piras
 */
public class RestResponseHelper {
    
    public ResponseEntity<Void> makeNoContentResponse() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    protected ResponseEntity<Void> makeResponse(UriComponentsBuilder builder, 
                                                String absolutePathWithIds, 
                                                Map<String, Object> uriVariables,
                                                HttpStatus httpStatus) {
        
        UriComponents uriComponents = builder.path(absolutePathWithIds).buildAndExpand(uriVariables);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(headers, httpStatus);
    }
}