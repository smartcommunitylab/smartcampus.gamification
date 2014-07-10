package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public interface ICustomizedPluginContainer {
    
    public Game getGame();
    public void setGame(Game game);
    
    public GamificationPlugin getGamificationPlugin();
    public void setGamificationPlugin(GamificationPlugin plugin);
}