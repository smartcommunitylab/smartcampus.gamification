package eu.trentorise.game.ruleengine.request;

import eu.trentorise.game.plugin.model.Plugin;

/**
 *
 * @author Luca Piras
 */
public class PluginOperatorRequest {
    
    protected Plugin gamificationPlugin;

    public Plugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(Plugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    @Override
    public String toString() {
        return "PluginOperatorRequest{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}