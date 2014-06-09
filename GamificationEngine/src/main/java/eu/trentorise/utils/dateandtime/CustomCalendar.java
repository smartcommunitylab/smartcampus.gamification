package eu.trentorise.utils.dateandtime;

import java.util.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Luca Piras
 */
public class CustomCalendar implements Comparable<CustomCalendar> {

    public static final Integer DAYS_MAX = new Integer(31);
    public static final Integer MONTHS_MAX = new Integer(12);
    public static final Integer HOURS_MAX = new Integer(24);
    public static final Integer MINUTES_MAX = new Integer(60);
    
    public static final Integer COUNT_YEARS_AFTER_CURRENT_DEFAULT = new Integer(1);
    public static final Integer START_VALUE_DEFAULT = new Integer(1);
    public static final Integer START_VALUE_HOURS_DEFAULT = new Integer(0);
    public static final Integer START_VALUE_MINUTES_DEFAULT = new Integer(0);    
    public static final Integer START_VALUE_SECONDS_DEFAULT = new Integer(0);
    public static final Integer START_VALUE_MILLISECONDS_DEFAULT = new Integer(0);
            
    protected Calendar calendar;
    protected Integer day;
    protected Integer month;
    protected Integer year;
    protected Integer hour;
    protected Integer minutes;
    
    protected List<Integer> days;
    protected List<Integer> months;
    protected List<Integer> years;
    
    protected List<Integer> hours;
    protected List<Integer> minutesPossible;
    
    protected String dateSeparator;
    protected String timeSeparator;
    
    protected ICalendarXMLGregorianCalendarConverter converter;
    
    public CustomCalendar(int year, int month, int day, int hour, int minutes,
                          String dateSeparator, String timeSeparator,
                          ICalendarXMLGregorianCalendarConverter converter) {
        
        this.init(null, dateSeparator, timeSeparator, converter);
        
        this.updateCalendar(year, month, day, hour, minutes);
    }
    
    public CustomCalendar(Integer countYearsAfterCurrent, String dateSeparator,
                          String timeSeparator, 
                          ICalendarXMLGregorianCalendarConverter converter) {
        
        this.init(countYearsAfterCurrent, dateSeparator, timeSeparator, 
                  converter);
    }
    
    public CustomCalendar(String dateSeparator, String timeSeparator,
                          ICalendarXMLGregorianCalendarConverter converter) {
        
        this.init(null, dateSeparator, timeSeparator, converter);
    }
    
    private void init(Integer countYearsAfterCurrent, String dateSeparator,
                      String timeSeparator, 
                      ICalendarXMLGregorianCalendarConverter converter) {
        
        if (null == countYearsAfterCurrent) {
            countYearsAfterCurrent = COUNT_YEARS_AFTER_CURRENT_DEFAULT;
        }
        
        this.calendar = new GregorianCalendar(Locale.ITALY);
        this.calendar.set(Calendar.SECOND, 
                          START_VALUE_SECONDS_DEFAULT);
        this.calendar.set(Calendar.MILLISECOND, 
                          START_VALUE_MILLISECONDS_DEFAULT);
        
        
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        //c'è un bug conosciuto in Calendar ed i suoi discendenti come 
        //GregorianCalendar, che riguarda i mesi, infatti inizia a calcolarli da
        //0, per cui è necessario fare degli aggiustamenti come di seguito
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        //this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.hour = START_VALUE_HOURS_DEFAULT;
        //this.minutes = calendar.get(Calendar.MINUTE);
        this.minutes = START_VALUE_MINUTES_DEFAULT;
        
        this.days = this.buildElements(DAYS_MAX, null);
        this.months = this.buildElements(MONTHS_MAX, null);
        this.years = this.buildElements(countYearsAfterCurrent + 1, this.year);
        
        this.hours = this.buildElements(HOURS_MAX, START_VALUE_HOURS_DEFAULT);
        this.minutesPossible = this.buildElements(MINUTES_MAX, START_VALUE_MINUTES_DEFAULT);
        
        /*System.out.println("number of day for that month of the year: " + calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        calendar = new GregorianCalendar(year, 1, day);
        System.out.println("time: " + calendar.getTime());
        System.out.println("number of day for that month of the year: " + calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));*/
        
        this.dateSeparator = dateSeparator;
        this.timeSeparator = timeSeparator;
        
        this.converter = converter;
        
        updateCalendar();
    }
    
    private void updateCalendar(int year, int month, int day, int hour, 
                                  int minutes) {
        
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
        this.setMinutes(minutes);
        
        updateCalendar();
    }
    
    public void updateCalendar() {
        //c'è un bug conosciuto in Calendar ed i suoi discendenti come 
        //GregorianCalendar, che riguarda i mesi, infatti inizia a calcolarli da
        //0, per cui è necessario fare degli aggiustamenti come di seguito
        this.calendar.set(this.year, this.month - 1, this.day, this.hour, 
                          this.minutes, START_VALUE_SECONDS_DEFAULT);
        
        this.calendar.set(Calendar.MILLISECOND, 
                          START_VALUE_MILLISECONDS_DEFAULT);
        
        this.refreshDays();
    }
    
    protected void refreshDays() {
        this.days = this.buildElements(calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH),
                                       null);
    }
    
    protected List<Integer> buildElements(Integer count, Integer startValue) {
        if (null == startValue) {
            startValue = START_VALUE_DEFAULT;
        }
        
        ArrayList<Integer> elements = new ArrayList<Integer>();
        
        for (int i = 0; i < count; i++) {
            Integer current = new Integer(startValue.intValue() + i);
            elements.add(current);
        }
        
        return elements;
    }
    
    public String getDate() {
        StringBuilder sb = new StringBuilder("");
        sb.append(this.addZero(this.day));
        sb.append(dateSeparator);
        sb.append(this.addZero(this.month));
        sb.append(dateSeparator);
        sb.append(this.year);
        
        return sb.toString();
    }
    
    public String getTime() {
        StringBuilder sb = new StringBuilder("");
        sb.append(this.addZero(this.hour));
        sb.append(timeSeparator);
        sb.append(this.addZero(this.minutes));
        
        return sb.toString();
    }
    
    protected String addZero(Integer value) {
        String result = value.toString();
        
        if (value.compareTo(new Integer(10)) < 0) {
            StringBuilder sb = new StringBuilder("0");
            sb.append(result);
            
            result = sb.toString();
        }
        
        return result;
    }
    
    public long getTimeInMillis() {
        this.updateCalendar();
        
        return this.calendar.getTimeInMillis();
    }
    
    public XMLGregorianCalendar toXMLGregorianCalendar() throws Exception {
        this.updateCalendar();
        
        return converter.convert(this.calendar);
    }
    
    @Override
    public int compareTo(CustomCalendar o) {
        this.updateCalendar();
        o.updateCalendar();
        
        return this.calendar.compareTo(o.calendar);
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setConverter(ICalendarXMLGregorianCalendarConverter converter) {
        this.converter = converter;
    }
    
    public List<Integer> getDays() {
        return days;
    }

    public List<Integer> getMonths() {
        return months;
    }

    public List<Integer> getYears() {
        return years;
    }

    public List<Integer> getHours() {
        return hours;
    }

    public List<Integer> getMinutesPossible() {
        return minutesPossible;
    }
    
    public String getDateSeparator() {
        return dateSeparator;
    }

    public String getTimeSeparator() {
        return timeSeparator;
    }

    public ICalendarXMLGregorianCalendarConverter getConverter() {
        return converter;
    }

    @Override
    public String toString() {
        return "CustomCalendar{" + "day=" + day + ", month=" + month + ", year=" + year + ", hour=" + hour + ", minutes=" + minutes + ", dateSeparator=" + dateSeparator + ", timeSeparator=" + timeSeparator + '}';
    }
}