package eu.trentorise.game.plugin.response;

import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public class CustomizedGamificationPluginResponse extends GameResponse {
    
    protected CustomizedGamificationPlugin customizedGamificationPlugin;

    public CustomizedGamificationPlugin getCustomizedGamificationPlugin() {
        return customizedGamificationPlugin;
    }

    public void setCustomizedGamificationPlugin(CustomizedGamificationPlugin customizedGamificationPlugin) {
        this.customizedGamificationPlugin = customizedGamificationPlugin;
    }

    @Override
    public String toString() {
        return "CustomizedGamificationPluginResponse{" + "customizedGamificationPlugin=" + customizedGamificationPlugin + '}';
    }
}