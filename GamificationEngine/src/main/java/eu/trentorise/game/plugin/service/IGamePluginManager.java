package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.container.ICustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.plugin.container.ICustomizedPluginListContainer;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginListResponse;
import eu.trentorise.game.plugin.response.GamificationPluginResponse;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGamePluginManager {
    
    public GamificationPluginResponse getGamificationPlugins();

    public CustomizedGamificationPluginListResponse getCustomizedGamificationPlugins(ICustomizedPluginListContainer container);

    public GameResponse activateDeactivateCustomizedGamificationPlugin(ICustomizedPluginActivationDeactivationContainer container);
}