package eu.trentorise.utils.rest;

import eu.trentorise.utils.rest.exception.ResourceException;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 * @param <T>
 */
public class RestResultHelper<T> {
    
    //CREATE
    public Boolean handleCreationResult(T result, Exception ex, Logger logger) {
        return handleResult(result, ex, logger);
    }
    
    public Boolean handleCreationResult(boolean isCreated, Exception ex, 
                                        Logger logger) {
        
        return handleResult(isCreated, ex, logger);
    }
    
    
    //READ
    public Boolean handleReadingResult(T result, Exception ex, Logger logger) {
        return handleResult(result, ex, logger);
    }
    
    public Boolean handleReadingResult(boolean isCreated, Exception ex, 
                                       Logger logger) {
        
        return handleResult(isCreated, ex, logger);
    }
    
    
    //GENERIC
    public Boolean handleResult(boolean isResultOk, Exception ex, 
                                       Logger logger) {
        
        if (null != ex || !isResultOk) {
            ResourceException exception = new ResourceException(ex);
            this.handleException(exception, logger);
            throw exception;
        }
        
        return true;
    }
    
    public Boolean handleResult(T result, Exception ex, Logger logger) {
        return this.handleResult((null != result), ex, logger);
    }

    
    //TOOLS
    protected void handleException(Exception ex, Logger logger) {
        StringBuilder sb = new StringBuilder("Exception: ").append(ex);
        
        Throwable cause = ex.getCause();
        if (null != cause) {
            sb.append(" - Cause: ").append(cause);
        }
        
        logger.warn(sb.toString());
    }
}