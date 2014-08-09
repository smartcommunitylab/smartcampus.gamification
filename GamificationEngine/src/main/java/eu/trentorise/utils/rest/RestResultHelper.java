package eu.trentorise.utils.rest;

import eu.trentorise.utils.rest.exception.ResourceException;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 * @param <T>
 */
public class RestResultHelper<T> {
    
    //GENERIC
    public Boolean handleResult(boolean isResultOk, Logger logger) throws Exception {
        
        if (!isResultOk) {
            throw new ResourceException();
        }
        
        return true;
    }
    
    public Boolean handleResult(T result, Logger logger) throws Exception {
        return this.handleResult((null != result), logger);
    }
}