package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.ruleengine.model.experimental.ExternalAction;
import eu.trentorise.game.ruleengine.model.experimental.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;



/**
 * 
 * @author Luca Piras
 */
@Repository("rovGameFactsDAO")
public class RovGameFactsDAO implements IFactsDAO {

    @Override
    public Collection getFacts() {
        List elements = new ArrayList();
        
        Map<String, Object> params = new HashMap<>();
        params.put("bikeKM", 20);
        params.put("busKM", 30);
        
        Player player = new Player();
        player.setId(1);
        player.setTotalPoints(11);
        
        ExternalAction extAct = new ExternalAction();
        extAct.setId(1);
        extAct.setParams(params);
        extAct.setPlayer(player);
        
        elements.add(extAct);
        
        return elements;
    }
}