package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.container.ICustomizedPluginContainer;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginListResponse;
import eu.trentorise.game.plugin.response.GamificationPluginListResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGamePluginManager {
    
    public GamificationPluginListResponse getGamificationPluginList();

    public CustomizedGamificationPluginListResponse getCustomizedGamificationPluginList(ICustomizedPluginContainer container);
}