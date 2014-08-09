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
                     Logger logger) throws Exception {
        
        Method method = manager.getClass().getMethod(managerMethodName, containerClass);
        T result = (T) method.invoke(manager, containerWithIds);
        restResultHelper.handleResult(result, logger);
        
        return result;
    }
}