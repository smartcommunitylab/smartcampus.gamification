package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.container.CustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.plugin.container.CustomizedPluginListContainer;
import eu.trentorise.game.plugin.container.ICustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.plugin.container.ICustomizedPluginListContainer;
import eu.trentorise.game.plugin.request.CustomizedPluginActivationDeactivationRequest;
import eu.trentorise.game.plugin.request.CustomizedPluginListRequest;
import eu.trentorise.game.plugin.response.CustomizedPluginListResponse;
import eu.trentorise.game.plugin.service.IPluginManager;
import eu.trentorise.game.response.GameResponse;
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
@Controller("customizedPluginController")
@RequestMapping(value=IGameConstants.SERVICE_PLUGINS_PATH)
public class CustomizedPluginController {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.POST, value = "/getCustomizedGamificationPlugins" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody CustomizedPluginListResponse getCustomizedGamificationPlugins(@RequestBody CustomizedPluginListRequest request) {
        ICustomizedPluginListContainer container = new CustomizedPluginListContainer();
        container.setGame(request.getGame());
        container.setGamificationPlugin(request.getGamificationPlugin());
        
        return gamePluginManager.getCustomizedGamificationPlugins(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/activateDeactivateCustomizedGamificationPlugin" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GameResponse activateDeactivateCustomizedGamificationPlugin(@RequestBody CustomizedPluginActivationDeactivationRequest request) {
        ICustomizedPluginActivationDeactivationContainer container = new CustomizedPluginActivationDeactivationContainer();
        container.setGame(request.getGame());
        container.setCustomizedGamificationPlugin(request.getCustomizedGamificationPlugin());
        container.setActivated(request.isActivated());
        
        return gamePluginManager.activateDeactivateCustomizedGamificationPlugin(container);
    }
    
    @Qualifier("mockGamePluginManager")
    @Autowired
    protected IPluginManager gamePluginManager;
}