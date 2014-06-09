package eu.trentorise.game.model.player;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luca Piras
 */
public class PlayerTest {
    
    public PlayerTest() {
    }
    
    /*@BeforeClass
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
     * Test of setPoints method, of class Player.
     */
    @Test
    public void testSetPoints() {
        System.out.println("setPoints");
        
        Player instance = new Player();
        
        assertEquals(new Integer(0), instance.getPoints());
        
        instance.setPoints(null);
        assertEquals(new Integer(0), instance.getPoints());
        
        instance.setPoints(0);
        assertEquals(new Integer(0), instance.getPoints());
        
        instance.setPoints(10);
        assertEquals(new Integer(10), instance.getPoints());
        
        instance.setPoints(-1);
        assertEquals(new Integer(-1), instance.getPoints());
    }
}