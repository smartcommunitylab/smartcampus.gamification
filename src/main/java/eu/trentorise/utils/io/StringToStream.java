package eu.trentorise.utils.io;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Luca Piras
 */
public class StringToStream implements IStringToStream {

    /**
     * Log4j Logger per il logging delle attività
     */
    private static Logger logger = LoggerFactory.getLogger(StringToStream.class.getName());

    public static final String UTF8 = "UTF-8";

    /*
     * Convert String to InputStream using ByteArrayInputStream
     * class. This class constructor takes the string byte array
     * which can be done by calling the getBytes() method.
     */
    @Override
    public InputStream stringToStream(String stringToConvert) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(stringToConvert.getBytes(UTF8));
    }

    /*
     * Convert String to InputStream using ByteArrayInputStream
     * class. This class constructor takes the string byte array
     * which can be done by calling the getBytes() method.
     */
    @Override
    public InputStream stringToStream(String stringToConvert,
                                      String charsetName) throws UnsupportedEncodingException {
        
        return new ByteArrayInputStream(stringToConvert.getBytes(charsetName));
    }
}