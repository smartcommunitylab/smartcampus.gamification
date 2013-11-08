package eu.trentorise.utils.converter.index;

/**
 *
 * @author Luca Piras
 */
public interface IIndexConverter {
    
    public static final int DEFAULT_COUNT_LETTERS = 26;
    
    public String indexToLetters(int index);
}