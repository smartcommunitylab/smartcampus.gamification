package eu.trentorise.utils.io;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Luca Piras
 */
public interface IStringToStream {
    
    public InputStream stringToStream(String stringToConvert) throws UnsupportedEncodingException;
    
    public InputStream stringToStream(String stringToConvert,
                                      String charsetName) throws UnsupportedEncodingException;
}