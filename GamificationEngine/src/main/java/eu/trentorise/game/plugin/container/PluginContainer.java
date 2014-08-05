package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.Plugin;

/**
 *
 * @author Luca Piras
 */
public class PluginContainer implements IPluginContainer {
    
    protected Plugin gamificationPlugin;

    @Override
    public Plugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    @Override
    public void setGamificationPlugin(Plugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    @Override
    public String toString() {
        return "GamificationPluginContainer{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}