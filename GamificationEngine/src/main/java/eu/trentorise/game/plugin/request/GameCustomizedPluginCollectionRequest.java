package eu.trentorise.game.plugin.request;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class GameCustomizedPluginCollectionRequest {
    
    protected Game game;

    protected CustomizedPlugin customizedPlugin;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public CustomizedPlugin getCustomizedPlugin() {
        return customizedPlugin;
    }

    public void setCustomizedPlugin(CustomizedPlugin customizedPlugin) {
        this.customizedPlugin = customizedPlugin;
    }

    @Override
    public String toString() {
        return "GameCustomizedPluginCollectionRequest{" + "game=" + game + ", customizedPlugin=" + customizedPlugin + '}';
    }
}