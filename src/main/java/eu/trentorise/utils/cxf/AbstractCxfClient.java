package eu.trentorise.utils.cxf;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractCxfClient<T, Z> {
    
    public abstract Z call(T container) throws Exception;
}