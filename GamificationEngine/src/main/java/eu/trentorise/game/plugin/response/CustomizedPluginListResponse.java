package eu.trentorise.game.plugin.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.response.*;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizedPluginListResponse extends GameResponse {
    
    protected List<CustomizedPlugin> customizedPlugins;

    public List<CustomizedPlugin> getCustomizedPlugins() {
        return customizedPlugins;
    }

    public void setCustomizedPlugins(List<CustomizedPlugin> customizedPlugins) {
        this.customizedPlugins = customizedPlugins;
    }

    @Override
    public String toString() {
        return "CustomizedPluginListResponse{" + "customizedPlugins=" + customizedPlugins + '}';
    }
}