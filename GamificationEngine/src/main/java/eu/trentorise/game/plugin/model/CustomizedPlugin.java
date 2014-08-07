package eu.trentorise.game.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizedPlugin extends Plugin {
    
    //TODO: id with the id of this customizedPlugin (the id provided by
    //the super class)
    protected Plugin plugin;

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String toString() {
        return "CustomizedPlugin{" + "plugin=" + plugin + '}';
    }
}