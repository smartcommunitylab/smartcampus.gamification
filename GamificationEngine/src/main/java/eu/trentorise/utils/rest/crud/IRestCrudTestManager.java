package eu.trentorise.utils.rest.crud;

import java.util.Collection;

/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 */
public interface IRestCrudTestManager<T, CC, C> {
    
    public T createElement(C containerWithIds) throws Exception;
    
    public Collection<T> createElements(CC containerWithIds) throws Exception;
}