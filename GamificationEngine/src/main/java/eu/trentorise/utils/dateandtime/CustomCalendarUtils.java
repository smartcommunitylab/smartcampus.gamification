package eu.trentorise.utils.dateandtime;

import org.joda.time.Period;


public class CustomCalendarUtils implements ICustomCalendarUtils {

    protected ICustomCalendarBuilder customCalendarBuilder;

    public CustomCalendarUtils() {
        customCalendarBuilder = new CustomCalendarBuilder();
    }
    
    /**
     * Questo metodo è stato pensato sulla base dei seguenti requisiti: 
     * per avere effettivamente il calcolo della durata tra 2 time è necessario
     * che startTime sia minore o uguale ad endTime; è implicito che entrambi i
     * time siano relativi allo stesso giorno in quanto non si hanno ulteriori
     * informazioni relativi alle date, che potrebbero consentire di funzionare
     * anche in assenza dei requisiti sopra citati
     * 
     * @param startTime time di inizio, deve essere sempre minore o uguale ad
     * endTime
     * @param endTime time di fine, deve essere sempre maggiore o uguale a 
     * startTime
     * @param dateSeparator separatore di data che si avrà nell'oggetto 
     * risultato
     * @param timeSeparator separatore di data che si ha nei 2 time passati e
     * nell'oggetto risultato
     * @return rende il calcolo della durata che intercorre tra il primo ed il
     * secondo time se e solo se i requisiti sopra citati sono soddisfatti,
     * altrimenti rende null (rende null anche se almeno uno tra startTime o
     * endTime è null o vuoto)
     * @throws Exception se qualcosa non va a buon fine
     */
    @Override
    public CustomCalendar calculateDuration(String startTime, String endTime,
                                            String dateSeparator,
                                            String timeSeparator) throws Exception {
        
        CustomCalendar result = null;
        
        if (null != startTime && !startTime.isEmpty() && 
            null != endTime && !endTime.isEmpty() && 
            null != dateSeparator && !dateSeparator.isEmpty() &&
            null != timeSeparator && !timeSeparator.isEmpty()) {
            
            CustomCalendar startCalendar = new CustomCalendar(dateSeparator, 
                                                          timeSeparator, null);
        
            startCalendar = customCalendarBuilder.build(startCalendar, startTime, 
                                                        timeSeparator);

            CustomCalendar endCalendar = customCalendarBuilder.build(startCalendar,
                                                                     endTime, 
                                                                     timeSeparator);

            if (startCalendar.compareTo(endCalendar) <= 0) {
                Period p = new Period(startCalendar.getTimeInMillis(), 
                                      endCalendar.getTimeInMillis());

                int resultHours = p.getHours();
                int resultMinutes = p.getMinutes();

                StringBuilder sb = new StringBuilder("");
                sb.append(resultHours).append(timeSeparator).append(resultMinutes);

                result = customCalendarBuilder.build(startCalendar, sb.toString(),
                                                     timeSeparator);
            }
        }
        
        return result;
    }

    /**
     * vedi calculateDuration con le startTime ed endTime come String
     * 
     * @param startHour
     * @param startMinutes
     * @param endHour
     * @param endMinutes
     * @param dateSeparator
     * @param timeSeparator
     * @return
     * @throws Exception 
     */
    @Override
    public CustomCalendar calculateDuration(int startHour, int startMinutes,
                                            int endHour, int endMinutes,
                                            String dateSeparator, 
                                            String timeSeparator) throws Exception {
        
        StringBuilder startSb = new StringBuilder();
        startSb.append(startHour).append(timeSeparator).append(startMinutes);
        
        StringBuilder endSb = new StringBuilder();
        endSb.append(endHour).append(timeSeparator).append(endMinutes);
        
        return calculateDuration(startSb.toString(), endSb.toString(), 
                                 dateSeparator, timeSeparator);
    }
}