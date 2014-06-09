package eu.trentorise.utils.converter.index;


public class IndexConverter implements IIndexConverter {

    @Override
    public String indexToLetters(int index) {
        String result = "";
        
        while (index >= 0) {
            int remainder = index % DEFAULT_COUNT_LETTERS;
            result = (char)(remainder + 'A') + result;
            index = (index / DEFAULT_COUNT_LETTERS) - 1;
        }
        
        return result;
    }
}