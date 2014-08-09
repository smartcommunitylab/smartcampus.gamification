package eu.trentorise.utils.rest;

import eu.trentorise.utils.rest.exception.ResourceException;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 */
public class RestExceptionHandler {
    
    public void handleException(Exception ex, Logger logger) {
        StringBuilder sb = new StringBuilder("Exception: ").append(ex);
        
        Throwable cause = ex.getCause();
        if (null != cause) {
            sb.append(" - Cause: ").append(cause);
        }
        
        logger.warn(sb.toString());
        
        throw new ResourceException(ex);
    }
}