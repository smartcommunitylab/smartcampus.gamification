package eu.trentorise.game.plugin.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.controller.AbstractCustomizedPluginController;
import eu.trentorise.game.plugin.point.request.PointPluginRequest;
import eu.trentorise.game.plugin.request.AbstractCustomizedPluginRequest;
import eu.trentorise.game.plugin.response.SettingCustomizedPluginResponse;
import eu.trentorise.game.plugin.service.ICustomizedPluginManager;
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
@Controller("pointPluginController")
@RequestMapping(IGameConstants.SERVICE_PLUGINS_POINT_PATH)
public class PointPluginController extends AbstractCustomizedPluginController {
    
    @RequestMapping(method = RequestMethod.POST, value = "/setCustomizedGamificationPlugin" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody SettingCustomizedPluginResponse setCustomizedGamificationPlugin(@RequestBody PointPluginRequest request) {
        return super.setCustomizedGamificationPlugin((AbstractCustomizedPluginRequest) request);
    }
    
    @Qualifier("mockPointPluginManager")
    @Autowired
    public void setManager(ICustomizedPluginManager manager) {
        this.manager = manager;
    }
}