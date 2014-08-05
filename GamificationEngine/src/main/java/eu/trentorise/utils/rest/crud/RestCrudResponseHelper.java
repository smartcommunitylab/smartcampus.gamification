package eu.trentorise.utils.rest.crud;

import eu.trentorise.utils.rest.RestResponseHelper;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Luca Piras
 */
public class RestCrudResponseHelper extends RestResponseHelper {

    //RESPONSE
    //this one returns the entity
    /*public ResponseEntity<T> makeCreationResponse(T result) {
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }*/
    
    public ResponseEntity<Void> makeCreationResponse(UriComponentsBuilder builder, 
                                                     String absolutePathWithIds, 
                                                     Map<String, Object> uriVariables) {
        
        return this.makeResponse(builder, absolutePathWithIds, uriVariables, 
                                 HttpStatus.CREATED);
    }
    
    public ResponseEntity<Void> makeUpdateResponse(UriComponentsBuilder builder, 
                                                   String absolutePathWithIds, 
                                                   Map<String, Object> uriVariables) {
        
        return this.makeResponse(builder, absolutePathWithIds, uriVariables, 
                                 HttpStatus.NO_CONTENT);
    }
    
    public ResponseEntity<Void> makeDeletionResponse() {
        return this.makeNoContentResponse();
    }
}