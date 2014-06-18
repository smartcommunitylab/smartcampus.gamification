package eu.trentorise.game.profile.game.model;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public class GameConfiguration {
    
    protected GameProfile gameProfile;
    protected GamificationPlugin plugin;
    
    protected boolean pluginActivated;

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public GamificationPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(GamificationPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isPluginActivated() {
        return pluginActivated;
    }

    public void setPluginActivated(boolean pluginActivated) {
        this.pluginActivated = pluginActivated;
    }

    @Override
    public String toString() {
        return "GameConfiguration{" + "gameProfile=" + gameProfile + ", plugin=" + plugin + ", pluginActivated=" + pluginActivated + '}';
    }
}