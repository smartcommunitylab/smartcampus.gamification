package eu.trentorise.utils.dateandtime;

/**
 *
 * @author Luca Piras
 */
public interface ICustomCalendarBuilder {
    
    public CustomCalendar build(CustomCalendar calendar, String time, 
                                String timeSeparator);
}