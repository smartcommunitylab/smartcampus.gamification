package eu.trentorise.game.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizedGamificationPlugin extends GamificationPlugin {
    
    protected GamificationPlugin gamificationPlugin;

    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(GamificationPlugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }
    
    @Override
    public String toString() {
        return "CustomizedGamificationPlugin{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}