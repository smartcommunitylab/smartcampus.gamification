package eu.trentorise.game.plugin.container;

import eu.trentorise.game.plugin.model.GameCustomizedPlugin;

/**
 *
 * @author Luca Piras
 */
public interface IGameCustomizedPluginCollectionContainer {
    
    public GameCustomizedPlugin getGameCustomizedPlugin();
    public void setGameCustomizedPlugin(GameCustomizedPlugin gameCustomizedPlugin);
}