package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.request.AbstractCustomizedPluginRequest;
import eu.trentorise.game.plugin.response.SettingCustomizedPluginResponse;
import eu.trentorise.game.plugin.service.ICustomizedPluginManager;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public abstract class AbstractCustomizedPluginController {
    
    public SettingCustomizedPluginResponse setCustomizedGamificationPlugin(AbstractCustomizedPluginRequest request) {
        CustomizedPluginContainer container = new CustomizedPluginContainer();
        container.setGame(request.getGame());
        container.setPlugin(request.getPlugin());
        
        return manager.setCustomizedGamificationPlugin(container);
    }
    
    protected ICustomizedPluginManager manager;
}