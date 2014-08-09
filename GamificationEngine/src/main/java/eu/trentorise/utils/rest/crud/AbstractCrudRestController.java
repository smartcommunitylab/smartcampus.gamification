package eu.trentorise.utils.rest.crud;

import eu.trentorise.utils.rest.RestExceptionHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 */
public abstract class AbstractCrudRestController<T, CC, C> {
    
    protected Logger logger;
    
    protected String resourceSinglePath;
    
    public AbstractCrudRestController(String resourceSinglePath, Logger logger) {
        this.resourceSinglePath = resourceSinglePath;
        
        this.logger = logger;
    }
    
    
    //CREATE
    public ResponseEntity<Void> createResource(C containerWithIds, 
                                               UriComponentsBuilder builder) {
        
        ResponseEntity<Void> response = null;
        try {
            T result = restCrudHelper.createSingleElement(containerWithIds, 
                                                          manager,
                                                          restCrudResultHelper,
                                                          logger);
            
            response = restCrudResponseHelper.makeCreationResponse(builder, 
                                                                   resourceSinglePath,
                                                                   this.makeUriVariables(containerWithIds, result));
        } catch (Exception ex) {
            restExceptionHandler.handleException(ex, logger);
        }
        
        return response;
    }
    
    
    //READ
    public Collection<T> readResources(CC containerWithIds) {
        Collection<T> response = null;
        
        try {
            response = restCrudHelper.readCollection(containerWithIds, manager, 
                                                     restCrudResultHelper,
                                                     logger);
            
        } catch (Exception ex) {
            restExceptionHandler.handleException(ex, logger);
        }
        
        return response;
    }
    
    
    public T readResourceById(C containerWithIds) {
        T response = null;
        
        try {
            response = restCrudHelper.readSingleElement(containerWithIds, 
                                                        manager, 
                                                        restCrudResultHelper, 
                                                        logger);
        } catch (Exception ex) {
            restExceptionHandler.handleException(ex, logger);
        }
        
        return response;
    }
    
    
    //UPDATE
    public ResponseEntity<Void> updateResource(C containerWithIds,
                                               UriComponentsBuilder builder) {
    
        ResponseEntity<Void> response = null;
        try {
            T result = restCrudHelper.updateSingleElement(containerWithIds, 
                                                          manager,
                                                          restCrudResultHelper, 
                                                          logger);
    
            response = restCrudResponseHelper.makeUpdateResponse(builder,
                                                                 resourceSinglePath,
                                                                 this.makeUriVariables(containerWithIds, result));
        } catch (Exception ex) {
            restExceptionHandler.handleException(ex, logger);
        }
        
        return response;
    }
    
    
    //DELETE
    public ResponseEntity<Void> deleteResource(C containerWithIds) {
        ResponseEntity<Void> response = null;
        try {
            restCrudHelper.deleteSingleElement(containerWithIds, manager, 
                                               restCrudResultHelper, logger);
    
            response = restCrudResponseHelper.makeDeletionResponse();
        } catch (Exception ex) {
            restExceptionHandler.handleException(ex, logger);
        }
        
        return response;
    }
    
    
    protected Map<String, Object> makeUriVariables(C containerWithIds, T result) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables = this.populateUriVariables(containerWithIds, result,
                                                 uriVariables);
        
        return uriVariables;
    }

    protected abstract Map<String, Object> populateUriVariables(C containerWithIds, 
                                                                T result,
                                                                Map<String, Object> uriVariables);
    
    
    protected IRestCrudManager<T, CC, C> manager;
    
    protected RestCrudHelper<T, CC, C> restCrudHelper;
    
    protected RestCrudResultHelper<T> restCrudResultHelper;
    
    protected RestCrudResponseHelper restCrudResponseHelper;
    
    protected RestExceptionHandler restExceptionHandler;
}