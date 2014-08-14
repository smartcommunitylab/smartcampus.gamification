package eu.trentorise.game.ruleengine.controller;


import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.ruleengine.container.GameRuleContainer;
import eu.trentorise.game.ruleengine.container.IGameRuleContainer;
import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.response.RuleCollectionResponse;
import eu.trentorise.game.ruleengine.response.RuleResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;


/**
 *
 * @author Luca Piras
 */
@Controller("ruleController")
public class RuleController extends AbstractCrudRestController<Rule, IGameRuleContainer, IGameRuleContainer> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public RuleController() {
        super(IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, 
              LoggerFactory.getLogger(RuleController.class.getName()));
    }
    
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createRule(@PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                                           @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                                           @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                                           @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                                           @RequestBody Rule element,
                                           UriComponentsBuilder builder) {
        
        //TODO: validate the ids
        
        IGameRuleContainer container = this.makeContainer(gameId,
                                                          plugId, 
                                                          cusPlugId,
                                                          ruleTemplId,
                                                          null,
                                                          element);
        
        return super.createResource(container, builder);
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_PATH, method = RequestMethod.GET)
    public @ResponseBody RuleCollectionResponse readRules(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId) {
        
        //TODO: validate the ids
        //TODO: this service will provide a list of rules related to a game 
        //(activated or deactivated, this is not important), to a ruleTemplate
        //and to a customizedPlugin (ruleTemplate and customizedPlugin are
        //inside the rule)
        
        IGameRuleContainer container = this.makeContainer(gameId,
                                                          plugId, 
                                                          cusPlugId,
                                                          ruleTemplId,
                                                          null,
                                                          null);
        
        Collection<Rule> results = super.readResources(container);
                                                
        RuleCollectionResponse response = new RuleCollectionResponse();
        response.setRules(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody RuleResponse readRuleById(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM) Integer ruleId) {
        
        //TODO: validate the ids
        
        IGameRuleContainer container = this.makeContainer(gameId,
                                                          plugId, 
                                                          cusPlugId,
                                                          ruleTemplId,
                                                          ruleId,
                                                          null);
        
        Rule result = super.readResourceById(container);
        
        RuleResponse response = new RuleResponse();
        response.setRule(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateRule(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM) Integer ruleId,
                         @RequestBody Rule element,
                         UriComponentsBuilder builder) {
        
        //TODO: validate the ids
        
        IGameRuleContainer container = this.makeContainer(gameId,
                                                          plugId, 
                                                          cusPlugId,
                                                          ruleTemplId,
                                                          ruleId,
                                                          element);
        
        return super.updateResource(container, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteRule(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM) Integer ruleId) {
        
        //TODO: validate the ids
        
        IGameRuleContainer container = this.makeContainer(gameId,
                                                          plugId, 
                                                          cusPlugId,
                                                          ruleTemplId,
                                                          ruleId,
                                                          null);
        
        return super.deleteResource(container);
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(IGameRuleContainer containerWithIds, Rule result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, containerWithIds.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, result.getRuleTemplate().getPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, result.getCustomizedPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM, result.getRuleTemplate().getId());
        uriVariables.put(IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
    protected IGameRuleContainer makeContainer(Integer gameId,
                                               Integer plugId,
                                               Integer cusPlugId, 
                                               Integer ruleTemplId, 
                                               Integer ruleId,
                                               Rule rule) {
        
        Game game = new Game();
        game.setId(gameId);
        
        Plugin plugin = new Plugin();
        plugin.setId(plugId);
        
        CustomizedPlugin customizedPlugin = new CustomizedPlugin();
        customizedPlugin.setId(cusPlugId);
        customizedPlugin.setPlugin(plugin);
        
        RuleTemplate ruleTemplate = new RuleTemplate();
        ruleTemplate.setId(ruleTemplId);
        ruleTemplate.setPlugin(plugin);
        
        if (null == rule) {
            rule = new Rule();
        }
        rule.setId(ruleId);
        rule.setCustomizedPlugin(customizedPlugin);
        rule.setRuleTemplate(ruleTemplate);
        
        IGameRuleContainer element = new GameRuleContainer();
        element.setGame(game);
        element.setRule(rule);
        
        return element;
    }
    
    
    @Qualifier("mockRuleManager")
    @Autowired
    public void setManager(IRestCrudManager<Rule, IGameRuleContainer, IGameRuleContainer> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<Rule, IGameRuleContainer, IGameRuleContainer> restCrudHelper) {
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