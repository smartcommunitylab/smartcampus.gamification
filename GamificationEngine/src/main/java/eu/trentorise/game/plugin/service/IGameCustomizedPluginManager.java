package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.container.ICustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameCustomizedPluginManager {
    
    public GameResponse activateDeactivateCustomizedGamificationPlugin(ICustomizedPluginActivationDeactivationContainer container);
}