package eu.trentorise.utils.rest;


import java.util.Collection;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
       
/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 */
public class RestResourceHelper<T, CC, C> {
    
    public Collection<T> findCollection(CC containerWithIds,
                                        IResourceManager<T, CC, C> manager,
                                        Logger logger) {
        
        Collection<T> result = null;
        Exception exception = null;
        try {
            result = manager.findCollection(containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResponseHelper.handleFindingResult(result, exception, logger);
        }
        
        return result;
    }
    
    public T findSingleElement(C containerWithIds,
                               IResourceManager<T, CC, C> manager,
                               Logger logger) {
        
        T result = null;
        Exception exception = null;
        try {
            result = manager.findSingleElement(containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResponseHelper.handleFindingResult(result, exception, logger);
        }
        
        return result;
    }
    
    @Qualifier("restResponseHelper")
    @Autowired
    protected RestResponseHelper restResponseHelper;
}