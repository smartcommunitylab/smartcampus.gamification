package eu.trentorise.utils.proxy;

import java.io.OutputStreamWriter;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;

public class AbstractCrossDomainProxy {

    protected Logger logger;

    protected String actualUrl;
    
    public AbstractCrossDomainProxy(Logger logger, String actualUrl) {
        this.logger = logger;
        this.actualUrl = actualUrl;
    }
    
    public void proxyAjaxCall(//@RequestParam(required = true, value = "xmlrequest") String xmlrequest,
                              HttpServletRequest request, 
                              HttpServletResponse response) throws Exception {
        
        //xmlrequest = "<?xml version='1.0' encoding='UTF-8' ?>" + xmlrequest;
        // URL needs to be url decoded
        //url = URLDecoder.decode(url, "utf-8");
        //xmlrequest = URLEncoder.encode(xmlrequest, "UTF-8");
        
        
        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
        HttpClient client = new HttpClient();
        try {

            String url = prepareGetUrl(actualUrl, request);
            
            //HttpMethod method = new PostMethod(actualUrl);
            //HttpMethod method = new GetMethod(actualUrl);
            HttpMethod method = new GetMethod(url);

            //POST
            // Set any eventual parameters that came with our original
            // request (POST params, for instance)
            /*Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                ((PostMethod) method).setParameter(paramName, 
                                                   request.getParameter(paramName));
            }*/
            
            //GET
            // Set any eventual parameters that came with our original
            // request (GET params, for instance)
            /*HttpMethodParams hmp = new HttpMethodParams();
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                hmp.setParameter(paramName, request.getParameter(paramName));
            }
            method.setParams(hmp);*/
                
            // Execute the method
            client.executeMethod(method);

            // Set the content type, as it comes from the server
            Header[] headers = method.getResponseHeaders();
            for (Header header : headers) {

                if ("Content-Type".equalsIgnoreCase(header.getName())) {

                    response.setContentType(header.getValue());
                }
            }

            // Write the body, flush and close
            writer.write(method.getResponseBodyAsString());
            writer.flush();
        } catch (Exception e) {

            logger.error("Errore nell'utilizzo di questo http proxy, IN " + 
                         "PRODUZIONE QUESTO PROXY NON DEVE ESSERE ABILITATO!!!", 
                         e);
            
            writer.write(e.toString());
            
            throw e;
        } finally {
            writer.close();
        }
    }

    protected String prepareGetUrl(String actualUrl, HttpServletRequest request) {
        StringBuilder returnValue = new StringBuilder(actualUrl);
        
        Enumeration paramNames = request.getParameterNames();
        boolean isTheFirstParam = true;
        while (paramNames.hasMoreElements()) {
            String paramDelimiter = "&";
            if (isTheFirstParam) {
                isTheFirstParam = false;
                paramDelimiter = "?";
            }
            returnValue.append(paramDelimiter);
            
            String paramName = (String) paramNames.nextElement();
            returnValue.append(paramName);
            returnValue.append("=");
            returnValue.append(request.getParameter(paramName));
        }
        
        return returnValue.toString();
    }
}