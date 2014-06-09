package eu.trentorise.utils.dateandtime;

import java.util.StringTokenizer;


public class StringDateIsoToEuropeanConverter implements IStringDateConverter {

    /**
     * From ISO format, yyyy-MM-dd, to european format, dd-MM-yyyy but and
     * viceversa
     */
    @Override
    public String convert(String date, String delimiter) {
        String converted = "";
        
        if (null == delimiter) {
            delimiter = IStringDateConverter.DEFAULT_DELIMITER;
        }
        
        if (null != date && !date.isEmpty()) {
            StringTokenizer st = new StringTokenizer(date, delimiter);
            
            while (st.hasMoreTokens()) {
                if (converted.isEmpty()) {
                    converted = st.nextToken();
                } else {
                    converted = st.nextToken() + delimiter + converted;
                }
            }
        }
        
        return converted;
    }
}