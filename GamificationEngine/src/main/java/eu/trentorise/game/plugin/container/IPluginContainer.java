package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.Plugin;

/**
 *
 * @author Luca Piras
 */
public interface IPluginContainer {
    
    public Plugin getGamificationPlugin();

    public void setGamificationPlugin(Plugin gamificationPlugin);
}