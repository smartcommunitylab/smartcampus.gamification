package eu.trentorise.utils.web;

import java.util.Map;

/**
 *
 * @author Luca Piras
 */
public interface IUrlMaker {
    
    public String makeUrl(String urlWithPathVariables, 
                          Map<String, Object> uriVariables);
}