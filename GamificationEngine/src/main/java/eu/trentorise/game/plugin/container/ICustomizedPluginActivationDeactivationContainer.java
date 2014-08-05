package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public interface ICustomizedPluginActivationDeactivationContainer {
    
    public Game getGame();
    public void setGame(Game game);

    public CustomizedPlugin getCustomizedGamificationPlugin();
    public void setCustomizedGamificationPlugin(CustomizedPlugin customizedGamificationPlugin);
    
    public boolean isActivated();
    public void setActivated(boolean activated);
}