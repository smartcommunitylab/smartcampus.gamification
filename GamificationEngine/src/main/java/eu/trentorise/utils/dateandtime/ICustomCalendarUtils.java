package eu.trentorise.utils.dateandtime;

/**
 *
 * @author Luca Piras
 */
public interface ICustomCalendarUtils {
    
    public CustomCalendar calculateDuration(String startTime, String endTime,
                                            String dateSeparator,
                                            String timeSeparator) throws Exception;
    
    public CustomCalendar calculateDuration(int startHour, 
                                            int startMinutes,
                                            int endHour, 
                                            int endMinutes,
                                            String dateSeparator,
                                            String timeSeparator) throws Exception;
    
}