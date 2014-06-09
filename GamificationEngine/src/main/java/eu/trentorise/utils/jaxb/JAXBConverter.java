package eu.trentorise.utils.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author Luca Piras
 */
public class JAXBConverter<T> implements IJAXBConverter<T> {

    private static Logger logger = LoggerFactory.getLogger(JAXBConverter.class.getName());
    
    @Override
    public String toXml(JAXBElement<T> element, Boolean formatResult) {
        String result = "";
        
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(element.getValue().getClass());
            Marshaller m = context.createMarshaller();
            m.marshal(element, writer);
            
            result = writer.toString();
            
            if (formatResult) {
                result = this.formatXml(result);
            }
        } catch (Exception ex) {
            logger.debug("Non è stato possibile rendere l'xml prodotto dall'oggetto:" + element.getValue(), ex);
        }
        
        return result;
    }
    
    @Override
    public String formatXml(String xml) {
        try{
            Transformer serializer= SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            //serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //serializer.setOutputProperty("{http://xml.customer.org/xslt}indent-amount", "2");
            Source xmlSource=new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res =  new StreamResult(new ByteArrayOutputStream());            
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream)res.getOutputStream()).toByteArray());
        }catch(Exception e){
            //log error (optional)
            return xml;
        }
    }
}