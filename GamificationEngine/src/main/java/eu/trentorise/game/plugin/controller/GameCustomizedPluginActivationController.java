package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.container.CustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.container.ICustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.request.CustomizedPluginActivationDeactivationRequest;
import eu.trentorise.game.plugin.service.IGameCustomizedPluginManager;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.utils.rest.crud.AbstractCrudRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("gameCustomizedPluginActivationController")
@RequestMapping(value = "/game/services/plugins/activateDeactivateCustomizedGamificationPlugin.service")
public class GameCustomizedPluginActivationController {
    
    @RequestMapping(method = RequestMethod.POST, value = "/activateDeactivateCustomizedGamificationPlugin" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GameResponse activateDeactivateCustomizedGamificationPlugin(@RequestBody CustomizedPluginActivationDeactivationRequest request) {
        ICustomizedPluginActivationDeactivationContainer container = new CustomizedPluginActivationDeactivationContainer();
        container.setGame(request.getGame());
        container.setCustomizedGamificationPlugin(request.getCustomizedGamificationPlugin());
        container.setActivated(request.isActivated());
        
        return gameCustomizedPluginManager.activateDeactivateCustomizedGamificationPlugin(container);
    }
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected IGameCustomizedPluginManager gameCustomizedPluginManager;
}