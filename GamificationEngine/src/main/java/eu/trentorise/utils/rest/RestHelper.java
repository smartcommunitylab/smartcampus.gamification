package eu.trentorise.utils.rest;

import java.lang.reflect.Method;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <C>
 */
public class RestHelper<T, C> {
    
    public T execute(C containerWithIds,
                     C manager,
                     String managerMethodName,
                     Class containerClass,
                     RestResultHelper<T> restResultHelper,
                     Logger logger) {
        
        T result = null;
        Exception exception = null;
        try {
            Method method = manager.getClass().getMethod(managerMethodName, containerClass);
            result = (T) method.invoke(manager, containerWithIds);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResultHelper.handleResult(result, exception, logger);
        }
        
        return result;
    }
}