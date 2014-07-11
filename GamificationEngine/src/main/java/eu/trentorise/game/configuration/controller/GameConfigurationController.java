package eu.trentorise.game.configuration.controller;

import eu.trentorise.game.configuration.response.ImportGamifiableActionsResponse;
import eu.trentorise.game.configuration.service.IGameConfigurationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.request.PluginActivationDeactivationRequest;
import eu.trentorise.game.response.GameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 * @author Luca Piras
 */
@Controller("gameConfigurationController")
@RequestMapping(IGameConstants.SERVICE_GAME_CONFIGURATION)
public class GameConfigurationController {

    @RequestMapping(method = RequestMethod.POST, value = "/importGamifiableActions" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody ImportGamifiableActionsResponse importGamifiableActions(@RequestParam(value = IGameConstants.PARAM_GAME_ID, required = true) Integer gameId,
                                                                                 @RequestParam(value = IGameConstants.PARAM_GAMIFIABLE_ACTIONS_FILE, required = true) MultipartFile multipartFile) {
        
        return manager.importGamifiableActions(null);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/activateDeactivatePlugin" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GameResponse activateDeactivatePlugin(@RequestBody PluginActivationDeactivationRequest co) {
        
        return manager.activateDeactivatePlugin(null);
    }
    
    @Qualifier("mockGameConfigurationManager")
    @Autowired
    protected IGameConfigurationManager manager;
}