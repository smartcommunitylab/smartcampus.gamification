package eu.trentorise.game.plugin.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.response.*;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizedGamificationPluginListResponse extends GameResponse {
    
    protected List<CustomizedGamificationPlugin> customizedGamificationPlugins;

    public List<CustomizedGamificationPlugin> getCustomizedGamificationPlugins() {
        return customizedGamificationPlugins;
    }

    public void setCustomizedGamificationPlugins(List<CustomizedGamificationPlugin> customizedGamificationPlugins) {
        this.customizedGamificationPlugins = customizedGamificationPlugins;
    }

    @Override
    public String toString() {
        return "CustomizedGamificationPluginListResponse{" + "customizedGamificationPlugins=" + customizedGamificationPlugins + '}';
    }
}