package eu.trentorise.game.profile.game.model;

import eu.trentorise.game.plugin.model.Plugin;

/**
 *
 * @author Luca Piras
 */
public class GameConfiguration {
    
    protected GameProfile gameProfile;
    protected Plugin plugin;
    
    protected boolean pluginActivated;

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
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