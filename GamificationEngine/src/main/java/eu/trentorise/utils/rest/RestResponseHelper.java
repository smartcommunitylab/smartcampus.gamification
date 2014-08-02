package eu.trentorise.utils.rest;

import eu.trentorise.utils.rest.exception.ResourceFindingException;
import java.util.Collection;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 * @param <T>
 */
public class RestResponseHelper<T> {

    public Boolean handleFindingResult(boolean isFound, Exception ex, 
                                       Logger logger) {
        
        if (null != ex || !isFound) {
            ResourceFindingException exception = new ResourceFindingException(ex);
            this.handleFindingException(exception, logger);
            throw exception;
        }
        
        return true;
    }

    public Boolean handleFindingResult(Collection result, Exception ex, 
                                       Logger logger) {
        
        return handleFindingResult((null != result && result.size() > 0), ex,
                                   logger);
    }
    
    public Boolean handleFindingResult(T result, Exception ex, Logger logger) {
        return handleFindingResult((null != result), ex, logger);
    }

    protected void handleFindingException(Exception ex, Logger logger) {
        StringBuilder sb = new StringBuilder("Exception: ").append(ex);
        
        Throwable cause = ex.getCause();
        if (null != cause) {
            sb.append(" - Cause: ").append(cause);
        }
        
        logger.error(sb.toString());
    }
}