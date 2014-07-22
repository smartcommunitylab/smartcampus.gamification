package eu.trentorise.game.plugin.point.data;

import eu.trentorise.game.event.model.StartEvent;
import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.ruleengine.data.IFactsDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author Luca Piras
 */
@Repository("mockPointFactsDAO")
public class MockPointFactsDAO implements IFactsDAO {

    @Override
    public Collection getFacts() {
        List elements = new ArrayList();
        
        StartEvent se = new StartEvent();
        
        ///////
        Player player = new Player();
        player.setUsername("firstPlayer");
        
        Action action = new Action();
        action.setId(1);
        
        se.setAction(action);
        se.setPlayer(player);
        elements.add(se);
        
        ///////
        se = new StartEvent();
        
        player = new Player();
        player.setUsername("secondPlayer");
        player.setPoints(new Integer(10));
        
        action = new Action();
        action.setId(1);
        
        se.setAction(action);
        se.setPlayer(player);
        elements.add(se);
        
        ///////
        se = new StartEvent();
        
        player = new Player();
        player.setUsername("thirdPlayer");
        player.setPoints(new Integer(100));
        
        action = new Action();
        action.setId(2);
        
        se.setAction(action);
        se.setPlayer(player);
        elements.add(se);
        
        ///////
        se = new StartEvent();
        
        player = new Player();
        player.setUsername("fourthPlayer");
        player.setPoints(new Integer(1000));
        
        action = new Action();
        action.setId(3);
        
        se.setAction(action);
        se.setPlayer(player);
        elements.add(se);
        
        return elements;
    }
}