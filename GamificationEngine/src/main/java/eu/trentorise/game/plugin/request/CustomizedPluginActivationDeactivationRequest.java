package eu.trentorise.game.plugin.request;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public class CustomizedPluginActivationDeactivationRequest {
    
    protected Game game;

    protected CustomizedPlugin customizedGamificationPlugin;
    
    protected boolean activated;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public CustomizedPlugin getCustomizedGamificationPlugin() {
        return customizedGamificationPlugin;
    }

    public void setCustomizedGamificationPlugin(CustomizedPlugin customizedGamificationPlugin) {
        this.customizedGamificationPlugin = customizedGamificationPlugin;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "CustomizedPluginActivationDeactivationRequest{" + "game=" + game + ", customizedGamificationPlugin=" + customizedGamificationPlugin + ", activated=" + activated + '}';
    }
}