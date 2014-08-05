package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class CustomizedPluginActivationDeactivationContainer implements ICustomizedPluginActivationDeactivationContainer {

    protected Game game;
    
    protected CustomizedPlugin customizedGamificationPlugin;

    protected boolean activated;
    
    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public CustomizedPlugin getCustomizedGamificationPlugin() {
        return customizedGamificationPlugin;
    }

    @Override
    public void setCustomizedGamificationPlugin(CustomizedPlugin customizedGamificationPlugin) {
        this.customizedGamificationPlugin = customizedGamificationPlugin;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "CustomizedGamificationPluginContainer{" + "game=" + game + ", customizedGamificationPlugin=" + customizedGamificationPlugin + ", activated=" + activated + '}';
    }
}