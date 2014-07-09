package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.controller.container.ICustomizedPluginContainer;
import eu.trentorise.game.plugin.response.GamificationPluginListResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGamePluginManager {
    
    public GamificationPluginListResponse getGamificationPluginList();

    public GamificationPluginListResponse getCustomizedGamificationPluginList(ICustomizedPluginContainer container);
}