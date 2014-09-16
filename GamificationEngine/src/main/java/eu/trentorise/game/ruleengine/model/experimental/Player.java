package eu.trentorise.game.ruleengine.model.experimental;

import java.util.Map;

/**
 *
 * @author Luca Piras
 */
public class Player {
    
    protected Integer id;
    
    //Integer gameName
    protected Map<String, PlayerState> gameStates;
    
    
    public PluginState getGameCustomizedPluginState(String gameName, Class pluginClass, String customizedPluginName) {
        return this.gameStates.get(gameName).getPluginStates().get(pluginClass).get(customizedPluginName);
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, PlayerState> getGameStates() {
        return gameStates;
    }

    public void setGameStates(Map<String, PlayerState> gameStates) {
        this.gameStates = gameStates;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", gameStates=" + gameStates + '}';
    }
}