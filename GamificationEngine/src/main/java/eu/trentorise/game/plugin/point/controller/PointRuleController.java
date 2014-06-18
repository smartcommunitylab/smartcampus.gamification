package eu.trentorise.game.plugin.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.plugin.point.model.rule.PointTemplateRule;
import eu.trentorise.game.plugin.point.service.IPointRuleManager;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.rule.model.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @author Luca Piras
 */
@Controller("pointRuleController")
@RequestMapping(IGameConstants.SERVICE_PLUGINS_POINT_PATH)
public class PointRuleController {
    
    @RequestMapping(method = RequestMethod.GET, value = "/addRule" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GameResponse addRule(@RequestParam(value = "actionId", required = true) Integer actionId,
                                                 @RequestParam(value = "rewardPoints", required = true) Integer rewardPoints) {
        
        Action action = new Action();
        action.setId(actionId);
        
        PointTemplateRule rule = new PointTemplateRule();
        
        //TODO: manage the id of the rule
        rule.setId(1);
        
        GamificationPlugin plugin = new GamificationPlugin();
        plugin.setName(GamificationPluginIdentifier.POINT_PLUGIN.toString());
        rule.setPlugin(plugin);
        
        rule.setAction(action);
        //TODO: manage validation of rewardPoints maybe with the check if it is
        //positive or negative or zero and so on
        rule.setRewardPoints(rewardPoints);
        
        return manager.add(rule);
    }
    
    @Qualifier("mockPointRuleManager")
    @Autowired
    protected IPointRuleManager manager;
}