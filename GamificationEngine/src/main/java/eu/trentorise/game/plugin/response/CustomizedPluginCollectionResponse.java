package eu.trentorise.game.plugin.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizedPluginCollectionResponse {
    
    protected Collection<CustomizedPlugin> customizedPlugins;

    public Collection<CustomizedPlugin> getCustomizedPlugins() {
        return customizedPlugins;
    }

    public void setCustomizedPlugins(Collection<CustomizedPlugin> customizedPlugins) {
        this.customizedPlugins = customizedPlugins;
    }

    @Override
    public String toString() {
        return "CustomizedPluginCollectionResponse{" + "customizedPlugins=" + customizedPlugins + '}';
    }
}