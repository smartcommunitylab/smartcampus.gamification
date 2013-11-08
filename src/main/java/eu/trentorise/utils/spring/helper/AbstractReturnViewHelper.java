package eu.trentorise.utils.spring.helper;

import org.apache.commons.lang.WordUtils;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractReturnViewHelper<I> {
    
    public String manageReturnView(I decisionalElement, String view) {
        String newView = view;
        
        if (this.makeDecision(decisionalElement)) {
            String additionalViewPart = buildAdditionalViewPart(decisionalElement);
            
            newView = buildNewView(view, additionalViewPart);
        }
        
        return newView;
    }
    
    protected abstract Boolean makeDecision(I decisionalElement);
    
    protected abstract String buildAdditionalViewPart(I decisionalElement);
    
    protected String buildNewView(String view, String additionalViewPart) {
        return view += WordUtils.capitalizeFully(additionalViewPart);
    }
}