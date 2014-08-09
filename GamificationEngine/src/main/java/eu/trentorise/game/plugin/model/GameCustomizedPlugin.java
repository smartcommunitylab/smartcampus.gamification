package eu.trentorise.game.plugin.model;

import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class GameCustomizedPlugin {
    
    //TODO: key
    protected Game game;
    
    //TODO: key
    protected CustomizedPlugin customizedPlugin;
    
    protected boolean activated;

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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "GameCustomizedPlugin{" + "game=" + game + ", customizedPlugin=" + customizedPlugin + ", activated=" + activated + '}';
    }
}