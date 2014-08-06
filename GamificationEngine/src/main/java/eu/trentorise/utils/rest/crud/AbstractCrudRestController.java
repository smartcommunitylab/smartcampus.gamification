package eu.trentorise.utils.rest.crud;

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
        
        T result = restCrudHelper.createSingleElement(containerWithIds, manager,
                                                      restCrudResultHelper,
                                                      logger);
    
        return restCrudResponseHelper.makeCreationResponse(builder, 
                                                           resourceSinglePath,
                                                           this.makeUriVariables(containerWithIds, result));
    }
    
    
    //READ
    public Collection<T> readResources(CC containerWithIds) {
        return restCrudHelper.readCollection(containerWithIds, manager, 
                                             restCrudResultHelper, logger);
    }
    
    
    public T readResourceById(C containerWithIds) {
        return restCrudHelper.readSingleElement(containerWithIds, manager, 
                                                restCrudResultHelper, logger);
    }
    
    
    //UPDATE
    public ResponseEntity<Void> updateResource(C containerWithIds,
                                               UriComponentsBuilder builder) {
        
        T result = restCrudHelper.updateSingleElement(containerWithIds, manager,
                                                      restCrudResultHelper, logger);
    
        return restCrudResponseHelper.makeUpdateResponse(builder,
                                                         resourceSinglePath,
                                                         this.makeUriVariables(containerWithIds, result));
    }
    
    
    //DELETE
    public ResponseEntity<Void> deleteResource(C containerWithIds) {
        restCrudHelper.deleteSingleElement(containerWithIds, manager, 
                                           restCrudResultHelper, logger);
    
        return restCrudResponseHelper.makeDeletionResponse();
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
}