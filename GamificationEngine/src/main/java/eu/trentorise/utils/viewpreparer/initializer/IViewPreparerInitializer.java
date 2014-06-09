package eu.trentorise.utils.viewpreparer.initializer;

import java.util.List;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;

/**
 *
 * @author Luca Piras
 */
public interface IViewPreparerInitializer {
    
    public void initializeNullAttribute(String attributeKey,
                                        TilesRequestContext tilesContext, 
                                        AttributeContext attributeContext);
    
    public void initializeNullAttributes(List<String> attributesKeys,
                                         TilesRequestContext tilesContext, 
                                         AttributeContext attributeContext);
}