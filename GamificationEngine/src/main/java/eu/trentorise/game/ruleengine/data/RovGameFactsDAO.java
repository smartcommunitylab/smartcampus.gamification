package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.ruleengine.model.experimental.ExternalAction;
import eu.trentorise.game.ruleengine.model.experimental.Game;
import eu.trentorise.game.ruleengine.model.experimental.Player;
import eu.trentorise.game.ruleengine.model.experimental.PlayerState;
import eu.trentorise.game.ruleengine.model.experimental.PluginState;
import eu.trentorise.game.ruleengine.model.experimental.PointPlugin;
import eu.trentorise.game.ruleengine.model.experimental.PointPluginState;
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
        
        //ExternalAction elements
        Map<String, Object> params = new HashMap<>();
        params.put("bikeKM", 20);
        params.put("busKM", 30);
        
        PointPluginState pointPluginState = new PointPluginState();
        pointPluginState.setTotalPoints(11);
        
        Integer greenLeavesId = 1;
        //Integer customizedPluginId
        Map<Integer, PluginState> rovGamePlayerCustomizedPluginStates = new HashMap<>();
        rovGamePlayerCustomizedPluginStates.put(greenLeavesId, pointPluginState);
        
        //Class pluginClass 
        //Integer pluginId
        Map<Class, Map<Integer, PluginState>> rovGamePlayerPluginStates = new HashMap<>();
        rovGamePlayerPluginStates.put(PointPlugin.class, rovGamePlayerCustomizedPluginStates);
        
        PlayerState rovGamePlayerState = new PlayerState();
        rovGamePlayerState.setPluginStates(rovGamePlayerPluginStates);
        
        Integer rovGameId = 1;
        
        Game game = new Game();
        game.setId(rovGameId);
        
        //Integer gameId
        Map<Integer, PlayerState> playerGameStates = new HashMap<>();
        playerGameStates.put(game.getId(), rovGamePlayerState);
        
        Player player = new Player();
        player.setId(1);
        player.setGameStates(playerGameStates);
        
        ExternalAction extAct = new ExternalAction();
        extAct.setId(1);
        extAct.setParams(params);
        extAct.setPlayer(player);
        
        elements.add(extAct);
        elements.add(game);
        
        //PointPluginState state = (PointPluginState) player.getGameStates().get(game.getId()).getPluginStates().get(PointPlugin.class).get(greenLeavesId);
        //state.setTotalPoints(state.getTotalPoints() + );
        
        
        
        return elements;
    }
}