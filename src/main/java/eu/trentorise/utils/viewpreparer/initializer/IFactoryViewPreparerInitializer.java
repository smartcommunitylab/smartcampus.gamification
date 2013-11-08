package eu.trentorise.utils.viewpreparer.initializer;

/**
 *
 * @author Luca Piras
 */
public interface IFactoryViewPreparerInitializer {
    
    public IViewPreparerInitializer getInitializer(Class attributeClass);
}