package eu.trentorise.utils.rest;

import eu.trentorise.utils.rest.exception.ResourceException;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 */
public class RestExceptionHandler {
    
    public void handleException(Exception ex, String actionNameExecuted, 
                                Logger logger) throws ResourceException {
        
        ResourceException resourceException = new ResourceException(ex);
        
        StringBuilder sb = new StringBuilder("Exception (actionNameExecuted: ");
        sb.append(actionNameExecuted).append("): ");
        sb.append(resourceException.getMessage()).append(" - ").append(ex);
        
        Throwable cause = ex.getCause();
        if (null != cause) {
            sb.append(" - Cause: ").append(cause);
        }
        
        logger.warn(sb.toString());
        
        throw resourceException;
    }
}