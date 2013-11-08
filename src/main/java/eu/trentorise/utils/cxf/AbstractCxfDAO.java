package eu.trentorise.utils.cxf;

import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractCxfDAO<T, Z, Y> {
        
    private Logger logger;
    
    protected AbstractCxfClient<T, Z> client;
    protected AbstractResponseHandler<T, Z, Y> responseHandler;
    
    public AbstractCxfDAO(AbstractCxfClient client, 
                          AbstractResponseHandler responseHandler,
                          Logger logger) {
        
        this.client = client;
        this.responseHandler = responseHandler;
        this.logger = logger;
    }
    
    public Y call(T container, Y result) throws Exception {
        
        boolean debugEnabled = logger.isDebugEnabled();
        if (debugEnabled) {
            StringBuilder sb = new StringBuilder("\nRequest value:\n");
            sb.append(container);
            this.logger.debug(sb.toString());
        }
        
        Z response = client.call(container);
        
        if (debugEnabled) {
            StringBuilder sb = new StringBuilder("\nResponse value:\n");
            sb.append(response);
            this.logger.debug(sb.toString());
        }
        
        return responseHandler.handle(container, response, result);
    }
}