package eu.trentorise.utils.dateandtime;

import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Luca Piras
 */
public interface ICalendarXMLGregorianCalendarConverter {
    
    public XMLGregorianCalendar convert(Calendar calendar) throws Exception;
}