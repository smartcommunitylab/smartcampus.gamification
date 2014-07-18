package eu.trentorise.game.plugin.request;

import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class CustomizedPluginActivationDeactivationRequest {
    
    protected Game game;

    protected CustomizedGamificationPlugin customizedGamificationPlugin;
    
    protected boolean activated;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public CustomizedGamificationPlugin getCustomizedGamificationPlugin() {
        return customizedGamificationPlugin;
    }

    public void setCustomizedGamificationPlugin(CustomizedGamificationPlugin customizedGamificationPlugin) {
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