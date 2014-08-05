package eu.trentorise.game.plugin.request;

import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class CustomizedPluginListRequest {
    
    protected Game game;

    protected Plugin gamificationPlugin;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Plugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(Plugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    @Override
    public String toString() {
        return "CustomizedPluginListRequest{" + "game=" + game + ", gamificationPlugin=" + gamificationPlugin + '}';
    }
}