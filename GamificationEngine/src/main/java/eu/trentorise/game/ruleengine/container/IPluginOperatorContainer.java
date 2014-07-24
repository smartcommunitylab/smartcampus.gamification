package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public interface IPluginOperatorContainer {
    
    public GamificationPlugin getGamificationPlugin();
    public void setGamificationPlugin(GamificationPlugin gamificationPlugin);
}