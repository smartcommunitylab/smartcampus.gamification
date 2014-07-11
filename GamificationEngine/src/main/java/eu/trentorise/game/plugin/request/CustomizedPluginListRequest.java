package eu.trentorise.game.plugin.request;

import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class CustomizedPluginListRequest {
    
    protected Game game;

    protected GamificationPlugin gamificationPlugin;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(GamificationPlugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    @Override
    public String toString() {
        return "CustomizedPluginListRequest{" + "game=" + game + ", gamificationPlugin=" + gamificationPlugin + '}';
    }
}