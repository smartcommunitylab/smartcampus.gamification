package eu.trentorise.game.plugin.controller.container;

import eu.trentorise.game.plugin.model.GamificationPlugin;


public class CustomizedPluginContainer implements ICustomizedPluginContainer {

    protected GamificationPlugin gamificationPlugin;
    
    @Override
    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    @Override
    public void setGamificationPlugin(GamificationPlugin plugin) {
        this.gamificationPlugin = plugin;
    }
}