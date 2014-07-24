package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public class PluginOperatorContainer implements IPluginOperatorContainer {
    
    protected GamificationPlugin gamificationPlugin;

    @Override
    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    @Override
    public void setGamificationPlugin(GamificationPlugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    @Override
    public String toString() {
        return "PluginOperatorContainer{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}