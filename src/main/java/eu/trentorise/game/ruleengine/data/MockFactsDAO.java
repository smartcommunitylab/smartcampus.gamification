package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.model.backpack.Badge;
import eu.trentorise.game.model.player.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author Luca Piras
 */
@Repository("mockFactsDAO")
public class MockFactsDAO implements IFactsDAO {

    @Override
    public Collection getFacts() {
        List elements = new ArrayList();
        
        Badge badge = new Badge("Basic Mayor", new Integer(10));
        
        elements.add(badge);
        
        badge = new Badge("Enhanced Mayor", new Integer(100));
        
        elements.add(badge);
        
        badge = new Badge("Advanced Mayor", new Integer(1000));
        
        elements.add(badge);
        
        Player player = new Player("firstPlayer");
        
        elements.add(player);
        
        player = new Player("secondPlayer");
        player.setPoints(new Integer(10));
        
        elements.add(player);
        
        player = new Player("thirdPlayer");
        player.setPoints(new Integer(100));
        
        elements.add(player);
        
        player = new Player("fourthPlayer");
        player.setPoints(new Integer(1000));
        
        elements.add(player);
        
        return elements;
    }
}