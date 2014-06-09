package eu.trentorise.utils.viewpreparer.initializer;

import java.util.List;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.slf4j.Logger;


public abstract class IAbstractViewPreparerInitializer<A> implements IViewPreparerInitializer {

    protected Logger logger;

    public IAbstractViewPreparerInitializer(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void initializeNullAttribute(String attributeKey, 
                                        TilesRequestContext tilesContext,
                                        AttributeContext attributeContext) {
        
        Attribute attribute = attributeContext.getAttribute(attributeKey);
        if (null == attribute) {
            attributeContext.putAttribute(attributeKey, 
                                          new Attribute(buildAttribute()));
        }/* else {
            
            Expression expressionObject = attribute.getExpressionObject();
            if (null != expressionObject) {
                
                String expression = expressionObject.getExpression();
                if (null != expression && !expression.isEmpty()) {
                    expression = expression.replace("${", "");
                    expression = expression.replace("}", "");
                    
                    StringTokenizer st = new StringTokenizer(expression, ".");
                    
                    String key = st.nextToken();
                    
                    Object value = tilesContext.getRequestScope().get(key);
                    
                    boolean initializeAttribute = false;
                    if (null == value) {
                        initializeAttribute = true;
                    } else {
                        while (st.hasMoreTokens() && !initializeAttribute) {
                            String next = st.nextToken();
                            try {
                                String stringMethod = "get" + WordUtils.capitalize(next);
                                Method method = value.getClass().getMethod(stringMethod);
                                value = method.invoke(value);
                                
                                initializeAttribute = (null == value);
                            } catch (Exception ex) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Java Reflection failure: " + ex);
                                }
                            } 
                        }
                    }
                    
                    if (initializeAttribute) {
                        attributeContext.putAttribute(attributeKey, 
                                                      new Attribute(buildAttribute()));
                    }
                }
            }
        }*/
    }

    @Override
    public void initializeNullAttributes(List<String> attributesKeys, 
                                         TilesRequestContext tilesContext,
                                         AttributeContext attributeContext) {
        
        for (String key : attributesKeys) {
            initializeNullAttribute(key, tilesContext, attributeContext);
        }
    }

    protected abstract A buildAttribute();
}