package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public interface ICustomizedPluginListContainer {
    
    public Game getGame();
    public void setGame(Game game);
    
    public Plugin getGamificationPlugin();
    public void setGamificationPlugin(Plugin plugin);
}