package eu.trentorise.utils.helper;

import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Luca Piras
 */
public class UrlParamsHelper implements IUrlParamsHelper {
    
    @Override
    public String getParamNameStartsWith(HttpServletRequest request, 
                                         String prefix) {
        
        String paramName = "";
        
        Enumeration parameterNames = request.getParameterNames();
        
        while (parameterNames.hasMoreElements() && paramName.isEmpty()) {
            String nextElement = (String) parameterNames.nextElement();
            
            if (nextElement.startsWith(prefix)) {
                paramName = nextElement;
            }
        }
        
        return paramName;
    }

    @Override
    public String getPostfixKey(String paramNameWithKeyAtTheEnd) {
        return this.getPostfixKey(paramNameWithKeyAtTheEnd, DEFAULT_DELIMITER);
    }
    
    @Override
    public String getPostfixKey(String paramNameWithKeyAtTheEnd, 
                                String delimiter) {
        
        String postfixIndex = "";
        
        if (null == delimiter || delimiter.isEmpty()) {
            delimiter = DEFAULT_DELIMITER;
        }
        
        StringTokenizer st = new StringTokenizer(paramNameWithKeyAtTheEnd, 
                                                 delimiter);

        while (st.hasMoreTokens()) {
            postfixIndex = st.nextToken();
        }
        
        return postfixIndex;
    }
}