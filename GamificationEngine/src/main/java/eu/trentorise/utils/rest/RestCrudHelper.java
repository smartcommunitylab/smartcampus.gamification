package eu.trentorise.utils.rest;


import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
       
/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 */
public class RestCrudHelper<T, CC, C> {
    
    //CREATE
    public T createSingleElement(C containerWithIds,
                                 ICrudManager<T, CC, C> manager,
                                 Logger logger) {
        
        T result = null;
        Exception exception = null;
        try {
            result = manager.createSingleElement(containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResultHelper.handleCreationResult(result, exception, logger);
        }
        
        return result;
    }
    
    //this one returns the entity
    /*public ResponseEntity<T> makeCreationResponse(T result) {
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }*/
    
    public ResponseEntity<Void> makeCreationResponse(UriComponentsBuilder builder, 
                                                     String absolutePathWithIds, 
                                                     Map<String, Object> uriVariables) {
        
        UriComponents uriComponents = builder.path(absolutePathWithIds).buildAndExpand(uriVariables);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
    
    //READ
    public Collection<T> readCollection(CC containerWithIds,
                                        ICrudManager<T, CC, C> manager,
                                        Logger logger) {
        
        Collection<T> result = null;
        Exception exception = null;
        try {
            result = manager.readCollection(containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResultHelper.handleReadingResult(result, exception, logger);
        }
        
        return result;
    }
    
    public T readSingleElement(C containerWithIds,
                               ICrudManager<T, CC, C> manager,
                               Logger logger) {
        
        T result = null;
        Exception exception = null;
        try {
            result = manager.readSingleElement(containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResultHelper.handleReadingResult(result, exception, logger);
        }
        
        return result;
    }
    
    
    @Qualifier("restResultHelper")
    @Autowired
    protected RestResultHelper restResultHelper;
}