package eu.trentorise.utils.viewpreparer.switcher;

import eu.trentorise.utils.viewpreparer.initializer.FactoryViewPreparerInitializer;
import eu.trentorise.utils.viewpreparer.initializer.IFactoryViewPreparerInitializer;
import java.util.List;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.AttributeContext;

public class SwitcherViewPreparer implements ViewPreparer {

    public static final String SCROLL_FLAG = "scrollFlag";
    
    public static final String DISABLED_INPUT_ELEMENTS = "switcherDisabledInputElements";

    IFactoryViewPreparerInitializer factory;
    
    public SwitcherViewPreparer() {
        factory = new FactoryViewPreparerInitializer();
    } 
    
    
    @Override
    public void execute(TilesRequestContext tilesContext, 
                        AttributeContext attributeContext) throws PreparerException {
        
        /*factory.getInitializer(String.class).initializeNullAttribute(SCROLL_FLAG,
                                                                     tilesContext,
                                                                     attributeContext);*/
        
        factory.getInitializer(List.class).initializeNullAttribute(DISABLED_INPUT_ELEMENTS,
                                                                   tilesContext,
                                                                   attributeContext);
    }
}