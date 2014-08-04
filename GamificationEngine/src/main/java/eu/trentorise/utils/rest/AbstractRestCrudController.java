package eu.trentorise.utils.rest;

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
public abstract class AbstractRestCrudController<T, CC, C> {
    
    protected Logger logger;
    
    protected String resourceSinglePath;
    
    public AbstractRestCrudController(String resourceSinglePath, Logger logger) {
        this.resourceSinglePath = resourceSinglePath;
        
        this.logger = logger;
    }
    
    //CREATE
    public ResponseEntity<Void> createResource(C containerWithIds, 
                                               UriComponentsBuilder builder) {
        
        T result = restCrudHelper.createSingleElement(containerWithIds, manager,
                                                      restResultHelper, logger);
    
        return restCrudHelper.makeCreationResponse(builder, 
                                                   resourceSinglePath,
                                                   this.makeUriVariables(containerWithIds, 
                                                                         result));
    }
    
    
    //READ
    public Collection<T> readResources(CC containerWithIds) {
        return restCrudHelper.readCollection(containerWithIds, manager, 
                                            restResultHelper, logger);
    }
    
    
    public T readResourceById(C containerWithIds) {
        return restCrudHelper.readSingleElement(containerWithIds, manager, 
                                                restResultHelper, logger);
    }
    
    
    //UPDATE
    public ResponseEntity<Void> updateResource(C containerWithIds,
                                               UriComponentsBuilder builder) {
        
        T result = restCrudHelper.updateSingleElement(containerWithIds, manager,
                                                      restResultHelper, logger);
    
        return restCrudHelper.makeUpdateResponse(builder, 
                                                 resourceSinglePath,
                                                 this.makeUriVariables(containerWithIds, 
                                                                       result));
    }
    
    
    //DELETE
    public ResponseEntity<Void> deleteResource(C containerWithIds) {
        restCrudHelper.deleteSingleElement(containerWithIds, manager, 
                                           restResultHelper, logger);
    
        return restCrudHelper.makeDeletionResponse();
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
    
    
    protected ICrudManager<T, CC, C> manager;
    
    protected RestCrudHelper<T, CC, C> restCrudHelper;
    
    protected RestResultHelper<T> restResultHelper;
}