package eu.trentorise.game.ruleengine.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IPluginOperatorContainer;
import eu.trentorise.game.ruleengine.container.IRuleContainer;
import eu.trentorise.game.ruleengine.container.IRuleTemplateContainer;
import eu.trentorise.game.ruleengine.container.OperatorContainer;
import eu.trentorise.game.ruleengine.container.PluginOperatorContainer;
import eu.trentorise.game.ruleengine.container.RuleContainer;
import eu.trentorise.game.ruleengine.container.RuleTemplateContainer;
import eu.trentorise.game.ruleengine.request.OperatorRequest;
import eu.trentorise.game.ruleengine.request.PluginOperatorRequest;
import eu.trentorise.game.ruleengine.request.RuleRequest;
import eu.trentorise.game.ruleengine.request.RuleTemplateRequest;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleResponse;
import eu.trentorise.game.ruleengine.response.RuleSettingResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import eu.trentorise.game.ruleengine.service.IRuleTemplateManager;
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
@Controller("ruleTemplateController")
@RequestMapping(IGameConstants.SERVICE_RULEENGINE_RULETEMPLATE_PATH)
public class RuleTemplateController {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.POST, value = "/getRuleTemplates" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody RuleTemplateResponse getRuleTemplates(@RequestBody RuleTemplateRequest request) throws Exception {
        //TODO: this service will provide a list of ruleTemplates related to the
        //gamificationPlugin specified in the request
        IRuleTemplateContainer container = new RuleTemplateContainer();
        container.setRuleTemplate(request.getRuleTemplate());
        
        return manager.getRuleTemplates(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getRules" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody RuleResponse getRules(@RequestBody RuleRequest request) throws Exception {
        //TODO: this service will provide a list of rules related to a game 
        //(activated or deactivated, this is not important), to a ruleTemplate
        //and to a customizedPlugin (ruleTemplate and customizedPlugin are
        //inside the rule)
        IRuleContainer container = new RuleContainer();
        container.setGame(request.getGame());
        container.setRule(request.getRule());
        
        return manager.getRules(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getOperatorsSupported" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody OperatorResponse getOperatorsSupported(@RequestBody OperatorRequest request) throws Exception {
        IOperatorContainer container = new OperatorContainer();
        container.setParam(request.getParam());
        container.setHandSideType(request.getHandSideType());
        
        return manager.getOperatorsSupported(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getPluginOperatorsSupported" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody OperatorResponse getPluginOperatorsSupported(@RequestBody PluginOperatorRequest request) throws Exception {
        //TODO: there are two ways to define operators. One is operators 
        //supported by the plugin to act in relation to the gamification concept
        //(for instance Points, operators for points) and the other one is
        //operators supported by the ruleTemplate of a plugin. Here we provide
        //at the moment only the first choice. If necessary will be implemented
        //also the other one
        IPluginOperatorContainer container = new PluginOperatorContainer();
        container.setGamificationPlugin(request.getGamificationPlugin());
        
        return manager.getPluginOperatorsSupported(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/setRule" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody RuleSettingResponse setRule(@RequestBody RuleRequest request) throws Exception {
        //TODO: set the rule, its relation with a game, and other internal 
        //relation (action, param, operator, ruleTemplate, etc. Furthermore,
        //create generate the content string, the rule in drools language and 
        //evaluate if it is possible to validate/verify the rule with drools
        //(compile, etc.), if it is sustainable and so on
        IRuleContainer container = new RuleContainer();
        container.setGame(request.getGame());
        container.setRule(request.getRule());
        
        return manager.setRule(container);
    }
    
    @Qualifier("mockRuleTemplateManager")
    @Autowired
    protected IRuleTemplateManager manager;
}