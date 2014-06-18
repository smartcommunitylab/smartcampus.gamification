package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.response.GamificationPluginsListResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGamePluginManager {
    
    public GamificationPluginsListResponse getGamificationPluginList();
}