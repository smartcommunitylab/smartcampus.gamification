package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.response.SettingCustomizedPluginResponse;

/**
 *
 * @author Luca Piras
 * @param <P>
 */
public interface ICustomizedPluginManager<P> {
    
    public SettingCustomizedPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<P> container);
}