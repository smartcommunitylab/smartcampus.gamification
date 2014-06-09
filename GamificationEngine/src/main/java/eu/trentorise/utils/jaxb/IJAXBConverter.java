package eu.trentorise.utils.jaxb;

import javax.xml.bind.JAXBElement;

/**
 *
 * @author Luca Piras
 */
public interface IJAXBConverter<T> {
    
    public String toXml(JAXBElement<T> element, Boolean formatResult);
    
    public String formatXml(String xml);
}