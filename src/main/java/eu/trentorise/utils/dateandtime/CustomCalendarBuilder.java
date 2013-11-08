package eu.trentorise.utils.dateandtime;

import java.util.StringTokenizer;


public class CustomCalendarBuilder implements ICustomCalendarBuilder {

    @Override
    public CustomCalendar build(CustomCalendar calendar, String time,
                                String timeSeparator) {
        
        StringTokenizer st = new StringTokenizer(time, timeSeparator);
        String hour = (String) st.nextElement();
        String minutes = (String) st.nextElement();
        
        return new CustomCalendar(calendar.getYear().intValue(), 
                                  calendar.getMonth().intValue(), 
                                  calendar.getDay().intValue(), 
                                  (new Integer(hour)).intValue(), 
                                  (new Integer(minutes)).intValue(), 
                                  calendar.getDateSeparator(), 
                                  calendar.getTimeSeparator(),
                                  calendar.getConverter());
    }
}