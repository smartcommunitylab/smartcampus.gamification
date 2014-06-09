package eu.trentorise.utils.cxf;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractResponseHandler<T, Z, Y> {
    
    protected abstract Y handle(T container, Z response, Y result) throws Exception;

    protected abstract void manageOkStatus(T container, Z response, 
                                           Y result) throws Exception;
    
    protected abstract void manageErrorStatus(T container, Z response,
                                              Y result) throws Exception;
    
    protected abstract void manageWarningStatus(T container, Z response,
                                                Y result) throws Exception;
    
    protected abstract void manageBadRequestStatus(T container, Z response,
                                                   Y result) throws Exception;
    
    protected abstract void manageGenericErrorStatus(T container, Z response,
                                                     Y result) throws Exception;
    
    protected void manageErrorPostedStatus(T container, Z response,
                                           Y result) throws Exception {
        
        this.manageBadRequestStatus(container, response, result);
    }
    
    protected void managePostedMissingParametersStatus(T container, Z response,
                                                       Y result) throws Exception {
        
        this.manageBadRequestStatus(container, response, result);
    }
    
    protected void manageUnexpectedStatus(T container, Z response,
                                          Y result) throws Exception {
        
        this.manageGenericErrorStatus(container, response, result);
    }
}