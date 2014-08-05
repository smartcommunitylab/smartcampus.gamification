package eu.trentorise.game.plugin.response;

import eu.trentorise.game.plugin.model.Plugin;

/**
 *
 * @author Luca Piras
 */
public class PluginResponse {
    
    protected Plugin plugin;

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String toString() {
        return "PluginResponse{" + "plugin=" + plugin + '}';
    }
}