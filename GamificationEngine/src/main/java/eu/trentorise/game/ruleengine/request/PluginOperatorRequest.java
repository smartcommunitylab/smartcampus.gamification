package eu.trentorise.game.ruleengine.request;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public class PluginOperatorRequest {
    
    protected GamificationPlugin gamificationPlugin;

    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(GamificationPlugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    @Override
    public String toString() {
        return "PluginOperatorRequest{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}