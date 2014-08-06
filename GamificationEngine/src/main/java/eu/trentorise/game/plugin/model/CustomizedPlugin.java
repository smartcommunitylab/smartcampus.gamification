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
    protected Plugin gamificationPlugin;

    public Plugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(Plugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }
    
    @Override
    public String toString() {
        return "CustomizedGamificationPlugin{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}