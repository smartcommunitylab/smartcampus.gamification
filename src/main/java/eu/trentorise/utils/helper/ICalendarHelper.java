package eu.trentorise.utils.helper;

import eu.trentorise.utils.dateandtime.CustomCalendar;
import org.springframework.ui.Model;

/**
 *
 * @author Luca Piras
 */
public interface ICalendarHelper {
    
    public static final String NOW_CALENDAR_KEY = "nowCalendar";
    
    public void addCalendarAttributes(Model model);
    
    public CustomCalendar createNowCalendar();
}