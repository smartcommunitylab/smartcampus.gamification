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
    public Boolean handleCreationResult(T result, Logger logger) throws Exception {
        return handleResult(result, logger);
    }
    
    public Boolean handleCreationResult(boolean isTaskOk, Logger logger) throws Exception {
        
        return handleResult(isTaskOk, logger);
    }
    
    
    //READ
    public Boolean handleReadingResult(T result, Logger logger) throws Exception {
        return handleResult(result, logger);
    }
    
    public Boolean handleReadingResult(boolean isTaskOk, Logger logger) throws Exception {
        
        return handleResult(isTaskOk, logger);
    }
    
    
    //CREATE
    public Boolean handleUpdateResult(T result, Logger logger) throws Exception {
        return handleResult(result, logger);
    }
    
    public Boolean handleUpdateResult(boolean isTaskOk, Logger logger) throws Exception {
        
        return handleResult(isTaskOk, logger);
    }
    
    
    //DELETION
    public Boolean handleDeletionResult(T result, Logger logger) throws Exception  {
        return handleResult(result, logger);
    }
    
    public Boolean handleDeletionResult(boolean isTaskOk, Logger logger) throws Exception {
        
        return handleResult(isTaskOk, logger);
    }
}