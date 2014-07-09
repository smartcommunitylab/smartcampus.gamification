package eu.trentorise.game.plugin.controller.container;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public interface ICustomizedPluginContainer {
    
    public GamificationPlugin getGamificationPlugin();
    
    public void setGamificationPlugin(GamificationPlugin plugin);
}