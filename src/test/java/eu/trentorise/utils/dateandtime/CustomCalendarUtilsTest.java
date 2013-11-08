package eu.trentorise.utils.dateandtime;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luca Piras
 */
public class CustomCalendarUtilsTest {
    
    protected ICustomCalendarUtils customCalendarUtils;
    
    public CustomCalendarUtilsTest() {}
    
    /*@BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }*/
    
    @Before
    public void setUp() {
        customCalendarUtils = new CustomCalendarUtils();
    }
    
    /*@After
    public void tearDown() {
    }*/

    /**
     * Test of testCalculateDurationWrongElements method, of class CustomCalendar.
     */
    @Test
    public void testCalculateDurationWrongElements() throws Exception {
        System.out.println("testCalculateDurationWrongElements");
        
        //first method
        Exception thrownException = null;
        try {
            customCalendarUtils.calculateDuration("05.07", "05.07", "-", ":");
        } catch(Exception ex) {
            thrownException = ex;
        }
        
        assertNotNull(thrownException);
        
        assertNull(customCalendarUtils.calculateDuration("05.07", "05.07", null, "."));
        assertNull(customCalendarUtils.calculateDuration("05.07", "05.07", "", "."));
        
        assertNull(customCalendarUtils.calculateDuration("05.07", "05.07", "-", ""));
        assertNull(customCalendarUtils.calculateDuration("05.07", "05.07", "-", null));
        
        CustomCalendar result = customCalendarUtils.calculateDuration(null, "05.07", "-", ":");
        
        assertNull(result);
        
        result = customCalendarUtils.calculateDuration("", "05.07", "-", ":");
        
        assertNull(result);
        
        result = customCalendarUtils.calculateDuration("05.07", null, "-", ":");
        
        assertNull(result);
        
        result = customCalendarUtils.calculateDuration("05.07", "", "-", ":");
        
        assertNull(result);
        //second method
        assertNull(customCalendarUtils.calculateDuration(5, 7, 5, 7, null, "."));
        assertNull(customCalendarUtils.calculateDuration(5, 7, 5, 7, "", "."));
        
        assertNull(customCalendarUtils.calculateDuration(5, 7, 5, 7, "-", ""));
        assertNull(customCalendarUtils.calculateDuration(5, 7, 5, 7, "-", null));
    }
    
    /**
     * Test of testCalculateDurationTheSame method, of class CustomCalendar.
     */
    @Test
    public void testCalculateDurationTheSame() throws Exception {
        System.out.println("testCalculateDurationTheSame");
        
        //first method
        CustomCalendar result = customCalendarUtils.calculateDuration("05.07", "05.07", "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(0), result.getMinutes());
        
        result = customCalendarUtils.calculateDuration("00.00", "00.00", "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(0), result.getMinutes());
        
        //second method
        result = customCalendarUtils.calculateDuration(5, 7, 5, 7, "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(0), result.getMinutes());
        
        result = customCalendarUtils.calculateDuration(0, 0, 0, 0, "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(0), result.getMinutes());
    }
    
    /**
     * Test of testCalculateDurationLesser method, of class CustomCalendar.
     */
    @Test
    public void testCalculateDurationDifferentLesser() throws Exception {
        System.out.println("testCalculateDurationDifferentLesser");
        
        //first method
        CustomCalendar result = customCalendarUtils.calculateDuration("05.07", "10.05", "-", ".");
        
        assertEquals(new Integer(4), result.getHour());
        assertEquals(new Integer(58), result.getMinutes());
        
        result = customCalendarUtils.calculateDuration("00.00", "00.01", "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(1), result.getMinutes());
        
        result = customCalendarUtils.calculateDuration("23.54", "23.59", "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(5), result.getMinutes());
        
        //second method
        result = customCalendarUtils.calculateDuration(5, 7, 10, 5, "-", ".");
        
        assertEquals(new Integer(4), result.getHour());
        assertEquals(new Integer(58), result.getMinutes());
        
        result = customCalendarUtils.calculateDuration(0, 0, 0, 1, "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(1), result.getMinutes());
        
        result = customCalendarUtils.calculateDuration(23, 54, 23, 59, "-", ".");
        
        assertEquals(new Integer(0), result.getHour());
        assertEquals(new Integer(5), result.getMinutes());
    }
    
    /**
     * Test of testBuildDurationDifferentUpper method, of class CustomCalendar.
     */
    @Test
    public void testCalculateDurationDifferentUpper() throws Exception {
        System.out.println("testCalculateDurationDifferentUpper");
        
        //first method
        CustomCalendar result = customCalendarUtils.calculateDuration("05.07", "02.35", "-", ".");
        
        assertNull(result);
        
        result = customCalendarUtils.calculateDuration("23.59", "00.00", "-", ".");
        
        assertNull(result);
        
        //second method
        result = customCalendarUtils.calculateDuration(5, 7, 2, 35, "-", ".");
        
        assertNull(result);
        
        result = customCalendarUtils.calculateDuration(23, 59, 0, 0, "-", ".");
        
        assertNull(result);
    }
}