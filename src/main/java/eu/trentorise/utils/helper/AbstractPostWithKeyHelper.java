package eu.trentorise.utils.helper;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;


public abstract class AbstractPostWithKeyHelper<I> {
    
    protected String paramNameFirstPart;
    
    public boolean managePostWithKey(HttpServletRequest request, I co, 
                                     Model model, 
                                     AbstractCommandHelper commandHelper) {
        
        Boolean changeHappened = Boolean.FALSE;
        
        String paramName = urlParamsHelper.getParamNameStartsWith(request, 
                                                                  this.paramNameFirstPart);
        
        if (null != paramName && !paramName.isEmpty()) {
            String postfixKey = urlParamsHelper.getPostfixKey(paramName);
            
            changeHappened = this.manageRetrievedKey(request, co, model,
                                                     postfixKey);
            
            if (changeHappened && null != commandHelper) {
                commandHelper.setCommand(paramName, model);
            }
        }
        
        return changeHappened;
    }
    
    public abstract boolean manageRetrievedKey(HttpServletRequest request, I co,
                                               Model model, String key);
    
    @Autowired
    protected IUrlParamsHelper urlParamsHelper;
}