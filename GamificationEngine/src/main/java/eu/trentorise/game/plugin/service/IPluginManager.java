package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.container.ICustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.plugin.container.ICustomizedPluginListContainer;
import eu.trentorise.game.plugin.response.CustomizedPluginListResponse;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IPluginManager {
    
    public CustomizedPluginListResponse getCustomizedGamificationPlugins(ICustomizedPluginListContainer container);

    public GameResponse activateDeactivateCustomizedGamificationPlugin(ICustomizedPluginActivationDeactivationContainer container);
}