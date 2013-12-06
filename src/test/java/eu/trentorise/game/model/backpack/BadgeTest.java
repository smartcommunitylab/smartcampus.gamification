package eu.trentorise.game.model.backpack;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author luca
 */
public class BadgeTest {
    
    public BadgeTest() {
    }
    
    /**
     * Test of getNecessaryPoints method, of class Badge.
     */
    @Test
    public void testPositiveValueNecessaryPointsInCreation() {
        System.out.println("testPositiveValueNecessaryPointsInCreation");
        
        Badge instance = new Badge("badge", new Integer(-10));
        assertEquals(new Integer(10), instance.getNecessaryPoints());
        
        instance = new Badge("badge", new Integer(-1));
        assertEquals(new Integer(1), instance.getNecessaryPoints());
        
        instance = new Badge("badge", new Integer(0));
        assertEquals(new Integer(0), instance.getNecessaryPoints());
        
        instance = new Badge("badge", new Integer(1));
        assertEquals(new Integer(1), instance.getNecessaryPoints());
        
        instance = new Badge("badge", new Integer(19));
        assertEquals(new Integer(19), instance.getNecessaryPoints());
    }
}