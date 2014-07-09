package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.co.CustomizedPluginListRequest;
import eu.trentorise.game.plugin.controller.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.controller.container.ICustomizedPluginContainer;
import eu.trentorise.game.plugin.response.GamificationPluginListResponse;
import eu.trentorise.game.plugin.service.IGamePluginManager;
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
@Controller("gamePluginController")
@RequestMapping(IGameConstants.SERVICE_PLUGINS_PATH)
public class GamePluginController {

    @RequestMapping(method = RequestMethod.POST, value = "/getGamificationPluginList" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GamificationPluginListResponse getGamificationPluginList() {
        
        return manager.getGamificationPluginList();
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getCustomizedGamificationPluginList" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GamificationPluginListResponse getCustomizedGamificationPluginList(@RequestBody CustomizedPluginListRequest co) {
        ICustomizedPluginContainer container = new CustomizedPluginContainer();
        container.setGamificationPlugin(co.getGamificationPlugin());
        
        return manager.getCustomizedGamificationPluginList(container);
    }
    
    @Qualifier("mockGamePluginManager")
    @Autowired
    protected IGamePluginManager manager;
}