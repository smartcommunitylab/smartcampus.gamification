package eu.trentorise.game.ruleengine.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IPluginOperatorContainer;
import eu.trentorise.game.ruleengine.container.IRuleContainer;
import eu.trentorise.game.ruleengine.container.OperatorContainer;
import eu.trentorise.game.ruleengine.container.PluginOperatorContainer;
import eu.trentorise.game.ruleengine.container.RuleContainer;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.request.OperatorRequest;
import eu.trentorise.game.ruleengine.request.PluginOperatorRequest;
import eu.trentorise.game.ruleengine.request.RuleRequest;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleResponse;
import eu.trentorise.game.ruleengine.response.RuleSettingResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateCollectionResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import eu.trentorise.game.ruleengine.service.IRuleTemplateManager;
import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.crud.AbstractCrudRestController;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.RestCrudHelper;
import eu.trentorise.utils.rest.crud.RestCrudResponseHelper;
import eu.trentorise.utils.rest.crud.RestCrudResultHelper;
import java.util.Collection;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("ruleTemplateController")
public class RuleTemplateController extends AbstractCrudRestController<RuleTemplate, Plugin, RuleTemplate> {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public RuleTemplateController() {
        super(IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH,
              LoggerFactory.getLogger(RuleTemplateController.class.getName()));
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_PATH, method = RequestMethod.GET)
    public @ResponseBody RuleTemplateCollectionResponse readRuleTemplates(
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId) {
        
        Plugin plugin = this.makePlugin(plugId);
        
        Collection<RuleTemplate> results = super.readResources(plugin);
                                                
        RuleTemplateCollectionResponse response = new RuleTemplateCollectionResponse();
        response.setRuleTemplates(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody RuleTemplateResponse readRuleTemplateById(
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId) {
        
        Plugin plugin = this.makePlugin(plugId);
        
        RuleTemplate element = new RuleTemplate();
        element.setId(ruleTemplId);
        element.setPlugin(plugin);
        
        RuleTemplate result = super.readResourceById(element);
        
        RuleTemplateResponse response = new RuleTemplateResponse();
        response.setRuleTemplate(result);
        
        return response;
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(RuleTemplate containerWithIds, RuleTemplate result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, result.getPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
    protected Plugin makePlugin(Integer plugId) {
        Plugin plugin = new Plugin();
        plugin.setId(plugId);
        return plugin;
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/game/services/ruleengine/ruletemplates" + "/getRules" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody RuleResponse getRules(@RequestBody RuleRequest request) throws Exception {
        //TODO: this service will provide a list of rules related to a game 
        //(activated or deactivated, this is not important), to a ruleTemplate
        //and to a customizedPlugin (ruleTemplate and customizedPlugin are
        //inside the rule)
        IRuleContainer container = new RuleContainer();
        container.setGame(request.getGame());
        container.setRule(request.getRule());
        
        return mockRuleTemplateManager.getRules(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/game/services/ruleengine/ruletemplates" + "/getOperatorsSupported" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody OperatorResponse getOperatorsSupported(@RequestBody OperatorRequest request) throws Exception {
        IOperatorContainer container = new OperatorContainer();
        container.setParam(request.getParam());
        container.setHandSideType(request.getHandSideType());
        
        return mockRuleTemplateManager.getOperatorsSupported(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/game/services/ruleengine/ruletemplates" + "/getPluginOperatorsSupported" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody OperatorResponse getPluginOperatorsSupported(@RequestBody PluginOperatorRequest request) throws Exception {
        //TODO: there are two ways to define operators. One is operators 
        //supported by the plugin to act in relation to the gamification concept
        //(for instance Points, operators for points) and the other one is
        //operators supported by the ruleTemplate of a plugin. Here we provide
        //at the moment only the first choice. If necessary will be implemented
        //also the other one
        IPluginOperatorContainer container = new PluginOperatorContainer();
        container.setGamificationPlugin(request.getGamificationPlugin());
        
        return mockRuleTemplateManager.getPluginOperatorsSupported(container);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/game/services/ruleengine/ruletemplates" + "/setRule" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody RuleSettingResponse setRule(@RequestBody RuleRequest request) throws Exception {
        //TODO: set the rule, its relation with a game, and other internal 
        //relation (action, param, operator, ruleTemplate, etc. Furthermore,
        //create generate the content string, the rule in drools language and 
        //evaluate if it is possible to validate/verify the rule with drools
        //(compile, etc.), if it is sustainable and so on
        IRuleContainer container = new RuleContainer();
        container.setGame(request.getGame());
        container.setRule(request.getRule());
        
        return mockRuleTemplateManager.setRule(container);
    }
    
    
    @Qualifier("mockRuleTemplateManager")
    @Autowired
    protected IRuleTemplateManager mockRuleTemplateManager;
    
    @Qualifier("mockRuleTemplateManager")
    @Autowired
    public void setManager(IRestCrudManager<RuleTemplate, Plugin, RuleTemplate> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<RuleTemplate, Plugin, RuleTemplate> restCrudHelper) {
        this.restCrudHelper = restCrudHelper;
    }

    @Qualifier("restCrudResultHelper")
    @Autowired
    public void setRestCrudResultHelper(RestCrudResultHelper restCrudResultHelper) {
        this.restCrudResultHelper = restCrudResultHelper;
    }
    
    @Qualifier("restCrudResponseHelper")
    @Autowired
    public void setRestCrudResponseHelper(RestCrudResponseHelper restCrudResponseHelper) {
        this.restCrudResponseHelper = restCrudResponseHelper;
    }
    
    @Qualifier("restExceptionHandler")
    @Autowired
    public void setRestExceptionHandler(RestExceptionHandler restExceptionHandler) {
        this.restExceptionHandler = restExceptionHandler;
    }
}