package eu.trentorise.utils.rest;


import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
       
/**
 *
 * @author Luca Piras
 * @param <T>
 */
public abstract class AbstractRestResourceController<T> {
    
    protected Logger logger;
    
    public AbstractRestResourceController(Logger logger) {
        this.logger = logger;
    }
    
    protected Collection<T> findCollection(List<String> ids) {
        Collection<T> result = null;
        Exception exception = null;
        try {
            result = this.serviceFindCollection(ids);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResponseHelper.handleFindingResult(result, exception, logger);
        }
        
        return result;
    }
    
    protected abstract Collection<T> serviceFindCollection(List<String> ids) throws Exception;
    
    protected T findSingleElement(List<String> ids) {
        T result = null;
        Exception exception = null;
        try {
            result = this.serviceFindSingleElement(ids);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResponseHelper.handleFindingResult(result, exception, logger);
        }
        
        return result;
    }
    
    protected abstract T serviceFindSingleElement(List<String> ids) throws Exception;
    
    
    @Qualifier("restResponseHelper")
    @Autowired
    protected RestResponseHelper restResponseHelper;
}