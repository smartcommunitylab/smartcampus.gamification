package eu.trentorise.utils.viewpreparer.initializer;

import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class FactoryViewPreparerInitializer implements IFactoryViewPreparerInitializer {
    
    @Override
    public IViewPreparerInitializer getInitializer(Class attributeClass) {
        IViewPreparerInitializer initializer = null;
        
        if (attributeClass.equals(String.class)) {
            initializer = new StringViewPreparerInitializer();
        } else if (attributeClass.equals(List.class)) {
            initializer = new ListViewPreparerInitializer();
        }
        
        return initializer;
    }
}