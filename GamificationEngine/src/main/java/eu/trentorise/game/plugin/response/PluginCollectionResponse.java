package eu.trentorise.game.plugin.response;

import eu.trentorise.game.plugin.model.Plugin;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class PluginCollectionResponse {
    
    protected Collection<Plugin> plugins;

    public Collection<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(Collection<Plugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public String toString() {
        return "PluginCollectionResponse{" + "plugins=" + plugins + '}';
    }
}