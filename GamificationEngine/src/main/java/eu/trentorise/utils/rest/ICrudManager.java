package eu.trentorise.utils.rest;

import java.util.Collection;

/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 */
public interface ICrudManager<T, CC, C> {
    
    public T createSingleElement(C containerWithForeignIds) throws Exception;
    
    public Collection<T> readCollection(CC containerWithIds) throws Exception;
    public T readSingleElement(C containerWithIds) throws Exception;
    
    public T updateSingleElement(C containerWithForeignIds) throws Exception;
    
    public T deleteSingleElement(C containerWithIds) throws Exception;
}