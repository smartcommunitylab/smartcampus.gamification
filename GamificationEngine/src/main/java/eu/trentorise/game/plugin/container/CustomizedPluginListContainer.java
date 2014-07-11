package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.profile.game.model.Game;


public class CustomizedPluginListContainer implements ICustomizedPluginListContainer {

    protected Game game;
    
    protected GamificationPlugin gamificationPlugin;
    
    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }
    
    @Override
    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    @Override
    public void setGamificationPlugin(GamificationPlugin plugin) {
        this.gamificationPlugin = plugin;
    }

    @Override
    public String toString() {
        return "CustomizedPluginContainer{" + "gamificationPlugin=" + gamificationPlugin + '}';
    }
}