package eu.trentorise.utils.dateandtime;
 
import java.util.Calendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class CalendarXMLGregorianCalendarConverter implements ICalendarXMLGregorianCalendarConverter {
    
    /**
     * Converts Calendar object into XMLGregorianCalendar
     *
     * @param calendar Object to be converted
     * @return XMLGregorianCalendar
     */
    @Override
    public XMLGregorianCalendar convert(Calendar calendar) throws Exception {
        
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            XMLGregorianCalendar xgc = dtf.newXMLGregorianCalendar();
            xgc.setYear(calendar.get(Calendar.YEAR));
            xgc.setMonth(calendar.get(Calendar.MONTH) + 1);
            xgc.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            xgc.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            xgc.setMinute(calendar.get(Calendar.MINUTE));
            xgc.setSecond(calendar.get(Calendar.SECOND));
            xgc.setMillisecond(calendar.get(Calendar.MILLISECOND));
 
            // Calendar ZONE_OFFSET and DST_OFFSET fields are in milliseconds.
            int offsetInMinutes = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
            xgc.setTimezone(offsetInMinutes);
            return xgc;
    }
}