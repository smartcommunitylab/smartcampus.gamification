package eu.trentorise.game.ruleengine.model.experimental;

import java.util.Map;

/**
 *
 * @author Luca Piras
 */
public class PlayerState {
    
    //Class pluginClass 
    //Integer pluginName
    protected Map<Class, Map<String, PluginState>> pluginStates;

    public Map<Class, Map<String, PluginState>> getPluginStates() {
        return pluginStates;
    }

    public void setPluginStates(Map<Class, Map<String, PluginState>> pluginStates) {
        this.pluginStates = pluginStates;
    }

    @Override
    public String toString() {
        return "PlayerState{" + "pluginStates=" + pluginStates + '}';
    }
}