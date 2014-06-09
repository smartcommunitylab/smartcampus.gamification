package eu.trentorise.utils.jaxb;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.xml.bind.JAXBElement;
import org.slf4j.Logger;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractJAXBJerseyDAO<T, V, Z, Y> {
    
    protected static final String WEB_RESOURCE_TYPE = "application/xml";
        
    private Logger logger;
    
    protected Client client;    
    protected String baseUrl;
    protected WebResource webResource;
    protected IJAXBConverter jaxbConverter;
    
    public AbstractJAXBJerseyDAO(Client client, String baseUrl, 
                                 IJAXBConverter jaxbConverter, Logger logger) {
        
        this.client = client;
        this.baseUrl = baseUrl;
        this.jaxbConverter = jaxbConverter;
        this.logger = logger;
        
        this.webResource = this.client.resource(this.baseUrl);
    }
    
    public Y call(T container, Class responseClass, Y result) throws Exception {
        
        V request = this.buildRequest(container);
        JAXBElement<V> requestXml = this.buildRequestJAXB(request);
        
        boolean debugEnabled = logger.isDebugEnabled();
        if (debugEnabled) {
            StringBuilder sb = new StringBuilder("\nRequest url: ");
            sb.append(this.baseUrl);
            sb.append("\nRequest xml value:\n");
            sb.append(jaxbConverter.toXml(requestXml, Boolean.TRUE));
            this.logger.debug(sb.toString());
        }
        
        Z response = (Z) webResource.type(WEB_RESOURCE_TYPE).post(responseClass, requestXml);
        
        if (debugEnabled) {
            StringBuilder sb = new StringBuilder("\nResponse xml value:\n");
            sb.append(jaxbConverter.toXml(this.buildResponseJAXB(response), Boolean.TRUE));
            this.logger.debug(sb.toString());
        }
        
        return this.buildResult(container, request, response, result);
    }
    
    protected abstract V buildRequest(T container) throws Exception;
    
    protected abstract JAXBElement<V> buildRequestJAXB(V request) throws Exception;
    
    protected abstract JAXBElement<Z> buildResponseJAXB(Z response) throws Exception;
    
    protected abstract Y buildResult(T container, V request, Z response, 
                                     Y result) throws Exception;
}