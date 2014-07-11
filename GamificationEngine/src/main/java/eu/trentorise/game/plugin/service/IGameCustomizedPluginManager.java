package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.point.controller.CustomizedPluginContainer;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameCustomizedPluginManager<P> {
    
    public CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<P> container);
}