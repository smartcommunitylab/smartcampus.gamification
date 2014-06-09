package eu.trentorise.utils.filter;

import java.util.List;

/**
 *
 * @author Luca Piras
 */
public interface IElementsFilter<C, R, E> {
    
    public void filter(C container, R result, 
                       List<E> elements) throws Exception;
}