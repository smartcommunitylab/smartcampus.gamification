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
    
    
    //READ
    public Collection<T> readCollection(CC containerWithIds,
                                        IRestCrudManager<T, CC, C> manager,
                                        RestCrudResultHelper restResultHelper,
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
                               IRestCrudManager<T, CC, C> manager,
                               RestCrudResultHelper<T> restResultHelper,
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
    
    
    //UPDATE
    public T updateSingleElement(C containerWithIds,
                                 IRestCrudManager<T, CC, C> manager,
                                 RestCrudResultHelper<T> restResultHelper,
                                 Logger logger) {
        
        T result = null;
        Exception exception = null;
        try {
            result = manager.updateSingleElement(containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResultHelper.handleUpdateResult(result, exception, logger);
        }
        
        return result;
    }
    
    
    //DELETE
    public T deleteSingleElement(C containerWithIds,
                                 IRestCrudManager<T, CC, C> manager,
                                 RestCrudResultHelper<T> restResultHelper,
                                 Logger logger) {
        
        T result = null;
        Exception exception = null;
        try {
            result = manager.deleteSingleElement(containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResultHelper.handleDeletionResult(result, exception, logger);
        }
        
        return result;
    }
}