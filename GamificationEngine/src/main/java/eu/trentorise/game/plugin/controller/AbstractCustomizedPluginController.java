package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.plugin.point.controller.CustomizedPluginContainer;
import eu.trentorise.game.plugin.request.AbstractCustomizedPluginRequest;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.plugin.service.IGameCustomizedPluginManager;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractCustomizedPluginController {
    
    public CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(AbstractCustomizedPluginRequest request) {
        CustomizedPluginContainer container = new CustomizedPluginContainer();
        container.setGame(request.getGame());
        container.setPlugin(request.getPlugin());
        
        return manager.setCustomizedGamificationPlugin(container);
    }
    
    protected IGameCustomizedPluginManager manager;
}