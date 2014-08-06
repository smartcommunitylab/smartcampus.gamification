package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.GameCustomizedPlugin;


public class GameCustomizedPluginCollectionContainer implements IGameCustomizedPluginCollectionContainer {

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
        return "GameCustomizedPluginCollectionContainer{" + "gameCustomizedPlugin=" + gameCustomizedPlugin + '}';
    }
}