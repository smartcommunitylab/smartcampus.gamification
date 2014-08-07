package eu.trentorise.game.plugin.response;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public class SettingCustomizedPluginResponse extends GameResponse {
    //TODO: remove this class and use the CustomizedPuginResponse one
    
    protected CustomizedPlugin customizedPlugin;

    public CustomizedPlugin getCustomizedPlugin() {
        return customizedPlugin;
    }

    public void setCustomizedPlugin(CustomizedPlugin customizedPlugin) {
        this.customizedPlugin = customizedPlugin;
    }

    @Override
    public String toString() {
        return "SettingCustomizedPuginResponse{" + "customizedPlugin=" + customizedPlugin + '}';
    }
}