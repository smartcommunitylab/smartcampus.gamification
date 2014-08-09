package eu.trentorise.utils.rest.crud;


import eu.trentorise.utils.rest.RestHelper;
import java.util.Collection;
import org.slf4j.Logger;
       
/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 */
public class RestCrudHelper<T, CC, C> extends RestHelper<T, C> {
    
    //CREATE
    public T createSingleElement(C containerWithIds,
                                 IRestCrudManager<T, CC, C> manager,
                                 RestCrudResultHelper<T> restResultHelper,
                                 Logger logger) throws Exception {
        
        
        T result = manager.createSingleElement(containerWithIds);
        restResultHelper.handleCreationResult(result, logger);
        
        return result;
    }
    
    
    //READ
    public Collection<T> readCollection(CC containerWithIds,
                                        IRestCrudManager<T, CC, C> manager,
                                        RestCrudResultHelper restResultHelper,
                                        Logger logger) throws Exception {
        
        
        Collection<T> result = manager.readCollection(containerWithIds);
        restResultHelper.handleReadingResult(result, logger);
        
        
        return result;
    }
    
    public T readSingleElement(C containerWithIds,
                               IRestCrudManager<T, CC, C> manager,
                               RestCrudResultHelper<T> restResultHelper,
                               Logger logger) throws Exception {
        
        T result = manager.readSingleElement(containerWithIds);
        restResultHelper.handleReadingResult(result, logger);
        
        return result;
    }
    
    
    //UPDATE
    public T updateSingleElement(C containerWithIds,
                                 IRestCrudManager<T, CC, C> manager,
                                 RestCrudResultHelper<T> restResultHelper,
                                 Logger logger) throws Exception {
        
        T result = manager.updateSingleElement(containerWithIds);
        restResultHelper.handleUpdateResult(result, logger);
        
        return result;
    }
    
    
    //DELETE
    public T deleteSingleElement(C containerWithIds,
                                 IRestCrudManager<T, CC, C> manager,
                                 RestCrudResultHelper<T> restResultHelper,
                                 Logger logger) throws Exception {
        
        T result = manager.deleteSingleElement(containerWithIds);
        restResultHelper.handleDeletionResult(result, logger);
        
        return result;
    }
}