package eu.trentorise.game.plugin.model;

import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class CustomizedPluginGame {
    
    protected Game game;
    
    protected CustomizedPlugin customizedGamificationPlugin;
    
    protected Boolean activated;

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

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "CustomizedPluginGame{" + "game=" + game + ", customizedGamificationPlugin=" + customizedGamificationPlugin + ", activated=" + activated + '}';
    }
}