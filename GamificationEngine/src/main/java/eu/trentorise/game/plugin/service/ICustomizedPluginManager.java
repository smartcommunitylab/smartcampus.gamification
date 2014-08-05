package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.response.CustomizedPluginResponse;

/**
 *
 * @author Luca Piras
 * @param <P>
 */
public interface ICustomizedPluginManager<P> {
    
    public CustomizedPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<P> container);
}