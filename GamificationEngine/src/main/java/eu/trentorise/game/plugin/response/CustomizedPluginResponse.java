package eu.trentorise.game.plugin.response;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public class CustomizedPluginResponse extends GameResponse {
    
    protected CustomizedPlugin customizedPlugin;

    public CustomizedPlugin getCustomizedPlugin() {
        return customizedPlugin;
    }

    public void setCustomizedPlugin(CustomizedPlugin customizedPlugin) {
        this.customizedPlugin = customizedPlugin;
    }

    @Override
    public String toString() {
        return "CustomizedPluginResponse{" + "customizedPlugin=" + customizedPlugin + '}';
    }
}