package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public interface ICustomizedPluginActivationDeactivationContainer {
    
    public Game getGame();
    public void setGame(Game game);

    public CustomizedGamificationPlugin getCustomizedGamificationPlugin();
    public void setCustomizedGamificationPlugin(CustomizedGamificationPlugin customizedGamificationPlugin);
    
    public boolean isActivated();
    public void setActivated(boolean activated);
}