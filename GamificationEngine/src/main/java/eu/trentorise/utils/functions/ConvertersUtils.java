package eu.trentorise.utils.functions;

import eu.trentorise.utils.converter.index.IndexConverter;

/**
 *
 * @author Luca Piras
 */
public class ConvertersUtils {
    
    public static String indexToLetters(int index) {
        return (new IndexConverter()).indexToLetters(index);
    }
}