package eu.trentorise.utils.helper;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Luca Piras
 */
public interface IUrlParamsHelper {
    
    public static final String DEFAULT_DELIMITER = "_";
    
    public String getParamNameStartsWith(HttpServletRequest request, 
                                         String prefix);
    
    public String getPostfixKey(String paramNameWithKeyAtTheEnd);
    
    public String getPostfixKey(String paramNameWithKeyAtTheEnd, 
                                String delimiter);
}