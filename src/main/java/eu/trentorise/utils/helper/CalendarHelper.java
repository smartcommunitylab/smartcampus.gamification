package eu.trentorise.utils.helper;

import eu.trentorise.utils.dateandtime.CustomCalendar;
import eu.trentorise.utils.dateandtime.ICalendarXMLGregorianCalendarConverter;
import org.springframework.ui.Model;

/**
 *
 * @author Luca Piras
 */
public class CalendarHelper implements ICalendarHelper {

    @Override
    public void addCalendarAttributes(Model model) {        
        model.addAttribute(NOW_CALENDAR_KEY, this.createNowCalendar());
    }
    
    @Override
    public CustomCalendar createNowCalendar() {
        return new CustomCalendar(dateSeparator, timeSeparator, converter);
    }

    public void setDateSeparator(String dateSeparator) {
        this.dateSeparator = dateSeparator;
    }

    public void setTimeSeparator(String timeSeparator) {
        this.timeSeparator = timeSeparator;
    }

    public void setConverter(ICalendarXMLGregorianCalendarConverter converter) {
        this.converter = converter;
    }
    
    protected String dateSeparator;
    
    protected String timeSeparator;
    
    protected ICalendarXMLGregorianCalendarConverter converter;
}