package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.GameCustomizedPlugin;

/**
 *
 * @author Luca Piras
 */
public class GameCustomizedPluginContainer implements IGameCustomizedPluginContainer {
    
    protected GameCustomizedPlugin gameCustomizedPlugin;

    @Override
    public GameCustomizedPlugin getGameCustomizedPlugin() {
        return gameCustomizedPlugin;
    }

    @Override
    public void setGameCustomizedPlugin(GameCustomizedPlugin gameCustomizedPlugin) {
        this.gameCustomizedPlugin = gameCustomizedPlugin;
    }

    @Override
    public String toString() {
        return "GameCustomizedPluginContainer{" + "gameCustomizedPlugin=" + gameCustomizedPlugin + '}';
    }
}