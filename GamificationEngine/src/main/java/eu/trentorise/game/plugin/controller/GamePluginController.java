package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.response.GamificationPluginsListResponse;
import eu.trentorise.game.plugin.service.IGamePluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("gamePluginController")
@RequestMapping(IGameConstants.SERVICE_PLUGINS_PATH)
public class GamePluginController {

    @RequestMapping(method = RequestMethod.POST, value = "/getGamificationPluginList" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GamificationPluginsListResponse getGamificationPluginList() {
        
        return manager.getGamificationPluginList();
    }
    
    @Qualifier("mockGamePluginManager")
    @Autowired
    protected IGamePluginManager manager;
}