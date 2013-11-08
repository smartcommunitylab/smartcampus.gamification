package eu.trentorise.utils.viewpreparer.initializer;

import org.slf4j.LoggerFactory;


public class StringViewPreparerInitializer extends IAbstractViewPreparerInitializer<String> {

    public StringViewPreparerInitializer() {
        super(LoggerFactory.getLogger(StringViewPreparerInitializer.class.getName()));
    }
    
    @Override
    protected String buildAttribute() {
        return new String();
    }
}