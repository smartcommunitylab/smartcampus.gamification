package eu.trentorise.utils.rest.crud;

import eu.trentorise.utils.rest.RestResultHelper;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 * @param <T>
 */
public class RestCrudResultHelper<T> extends RestResultHelper<T> {
    
    //CREATE
    public Boolean handleCreationResult(T result, Exception ex, Logger logger) {
        return handleResult(result, ex, logger);
    }
    
    public Boolean handleCreationResult(boolean isTaskOk, Exception ex, 
                                        Logger logger) {
        
        return handleResult(isTaskOk, ex, logger);
    }
    
    
    //READ
    public Boolean handleReadingResult(T result, Exception ex, Logger logger) {
        return handleResult(result, ex, logger);
    }
    
    public Boolean handleReadingResult(boolean isTaskOk, Exception ex, 
                                       Logger logger) {
        
        return handleResult(isTaskOk, ex, logger);
    }
    
    
    //CREATE
    public Boolean handleUpdateResult(T result, Exception ex, Logger logger) {
        return handleResult(result, ex, logger);
    }
    
    public Boolean handleUpdateResult(boolean isTaskOk, Exception ex, 
                                      Logger logger) {
        
        return handleResult(isTaskOk, ex, logger);
    }
    
    
    //DELETION
    public Boolean handleDeletionResult(T result, Exception ex, Logger logger) {
        return handleResult(result, ex, logger);
    }
    
    public Boolean handleDeletionResult(boolean isTaskOk, Exception ex, 
                                        Logger logger) {
        
        return handleResult(isTaskOk, ex, logger);
    }
}