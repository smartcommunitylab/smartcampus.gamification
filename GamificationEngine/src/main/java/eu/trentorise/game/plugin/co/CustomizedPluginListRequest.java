package eu.trentorise.game.plugin.co;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public class CustomizedPluginListRequest {
    
    protected GamificationPlugin gamificationPlugin;

    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(GamificationPlugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    @Override
    public String toString() {
        return "CustomizedPluginListRequest{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}