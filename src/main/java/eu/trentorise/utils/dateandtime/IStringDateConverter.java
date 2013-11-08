package eu.trentorise.utils.dateandtime;

/**
 *
 * @author Luca Piras
 */
public interface IStringDateConverter {
    
    public static final String DEFAULT_DELIMITER = "-";
    
    public String convert(String date, String delimiter);
}