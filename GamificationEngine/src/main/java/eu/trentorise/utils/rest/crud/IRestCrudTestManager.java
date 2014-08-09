package eu.trentorise.utils.rest.crud;

import java.util.Collection;

/**
 *
 * @author Luca Piras
 * @param <T>
 */
public interface IRestCrudTestManager<T> {
    
    public T createElement();
    
    public Collection<T> createElements();
}