package eu.trentorise.game.plugin.leaderboard.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.controller.AbstractCustomizedPluginController;
import eu.trentorise.game.plugin.leaderboard.point.request.LeaderboardPointPluginRequest;
import eu.trentorise.game.plugin.request.AbstractCustomizedPluginRequest;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.plugin.service.IGameCustomizedPluginManager;
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
@Controller("leadearboardPointPluginController")
@RequestMapping(IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_PATH)
public class LeadearboardPointPluginController extends AbstractCustomizedPluginController {
    
    @RequestMapping(method = RequestMethod.POST, value = "/setCustomizedGamificationPlugin" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(@RequestBody LeaderboardPointPluginRequest request) {
        return super.setCustomizedGamificationPlugin((AbstractCustomizedPluginRequest) request);
    }
    
    @Qualifier("mockLeaderboardPointPluginManager")
    @Autowired
    public void setManager(IGameCustomizedPluginManager manager) {
        this.manager = manager;
    }
}