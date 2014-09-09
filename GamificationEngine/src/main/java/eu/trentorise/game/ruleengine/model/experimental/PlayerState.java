package eu.trentorise.game.ruleengine.model.experimental;

import java.util.Map;

/**
 *
 * @author Luca Piras
 */
public class PlayerState {
    
    //Class pluginClass 
    //Integer pluginId
    protected Map<Class, Map<Integer, PluginState>> pluginStates;

    public Map<Class, Map<Integer, PluginState>> getPluginStates() {
        return pluginStates;
    }

    public void setPluginStates(Map<Class, Map<Integer, PluginState>> pluginStates) {
        this.pluginStates = pluginStates;
    }

    @Override
    public String toString() {
        return "PlayerState{" + "pluginStates=" + pluginStates + '}';
    }
}