package eu.trentorise.utils.filter;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;

public abstract class AbstractElementsFilter<C, R, E> implements IElementsFilter<C, R, E> {

    protected List<IElementFilter<C, R, E>> filters;
    
    protected Logger logger;

    public AbstractElementsFilter(List<IElementFilter<C, R, E>> filters, Logger logger) {
        this.filters = filters;
        this.logger = logger;
    }
    
    @Override
    public void filter(C container, R result, 
                       List<E> elements) throws Exception {
        
        for (Iterator<E> it = elements.iterator(); it.hasNext();) {
            E current = it.next();
            if (useFilters(container, result, current)) {
                filterElement(container, result, current, it);
                
                if (logger.isDebugEnabled()) {
                    logger.debug("\nFiltered element: " + current + "\n");
                }
            } else {
                dontFilterElement(container, result, current, it);
            }
        }
    }
    
    protected Boolean useFilters(C container, R result, E element) throws Exception {
        
        Boolean doFiltering = Boolean.FALSE;
        for (int j = 0; j < filters.size() && doFiltering == Boolean.FALSE; j++) {
            doFiltering = filters.get(j).filter(container, result, element);
        }
        
        return doFiltering;
    }
    
    protected void filterElement(C container, R result, E element, 
                                 Iterator<E> it) throws Exception {
        
        it.remove();
    }

    protected void dontFilterElement(C container, R result, E element, 
                                     Iterator<E> it) {}
}