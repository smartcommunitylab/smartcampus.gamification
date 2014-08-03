package eu.trentorise.utils.rest;

import java.util.Collection;

/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 */
public interface IResourceManager<T, CC, C> {
    
    public Collection<T> findCollection(CC containerWithIds) throws Exception;
    
    public T findSingleElement(C containerWithIds) throws Exception;
}