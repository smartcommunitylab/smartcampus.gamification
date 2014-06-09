package eu.trentorise.utils.converter.index;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luca Piras
 */
public class IndexConverterTest {
    
    /*public IndexConverterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }*/

    /**
     * Test of indexToLetters method, of class IndexConverter.
     */
    @Test
    public void testIndexToLetters() {
        System.out.println("indexToLetters");
        
        IndexConverter instance = new IndexConverter();
        
        int index = -2;
        assertEquals("", instance.indexToLetters(index++));
        assertEquals("", instance.indexToLetters(index++));
        
        assertEquals("A", instance.indexToLetters(index++));
        assertEquals("B", instance.indexToLetters(index++));
        index = 25;
        assertEquals("Z", instance.indexToLetters(index++));
        
        assertEquals("AA", instance.indexToLetters(index++));
        assertEquals("AB", instance.indexToLetters(index++));
        assertEquals("AC", instance.indexToLetters(index++));
        assertEquals("AD", instance.indexToLetters(index++));
        assertEquals("AE", instance.indexToLetters(index++));
        index = 51;
        assertEquals("AZ", instance.indexToLetters(index++));
        
        assertEquals("BA", instance.indexToLetters(index++));
        assertEquals("BB", instance.indexToLetters(index++));
        index = 77;
        assertEquals("BZ", instance.indexToLetters(index++));
        
        assertEquals("CA", instance.indexToLetters(index++));
        
        index = 26 * 27;
        assertEquals("AAA", instance.indexToLetters(index++));
        assertEquals("AAB", instance.indexToLetters(index++));
    }
}