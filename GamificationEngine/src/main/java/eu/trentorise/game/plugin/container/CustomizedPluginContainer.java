package eu.trentorise.game.plugin.container;

import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 * @param <P>
 */
public class CustomizedPluginContainer<P> {
    protected Game game;
    protected P plugin;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public P getPlugin() {
        return plugin;
    }

    public void setPlugin(P plugin) {
        this.plugin = plugin;
    }
}