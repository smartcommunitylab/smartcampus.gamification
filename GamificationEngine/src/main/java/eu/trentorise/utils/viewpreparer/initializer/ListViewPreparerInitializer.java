package eu.trentorise.utils.viewpreparer.initializer;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;


public class ListViewPreparerInitializer extends IAbstractViewPreparerInitializer<List> {

    public ListViewPreparerInitializer() {
        super(LoggerFactory.getLogger(ListViewPreparerInitializer.class.getName()));
    }
    
    @Override
    protected List buildAttribute() {
        return new ArrayList();
    }
}