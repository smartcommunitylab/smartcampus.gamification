package eu.trentorise.game.ruleengine.controller;


import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.ruleengine.model.GameRule;
import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.response.GameRuleCollectionResponse;
import eu.trentorise.game.ruleengine.response.GameRuleResponse;
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
@Controller("gameRuleController")
public class GameRuleController extends AbstractCrudRestController<GameRule, GameRule, GameRule> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public GameRuleController() {
        super(IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, 
              LoggerFactory.getLogger(GameRuleController.class.getName()));
    }
    
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createGameRule(@PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                                               @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                                               @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                                               @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                                               @RequestBody GameRule element,
                                               UriComponentsBuilder builder) {
        
        //TODO: validate the ids
        //TODO: create the new rule provided and create an instance in gameRule
        //in order to associate the game indicated with the rule provided
        //(as default behavior set activated to true)
        GameRule gameRule = this.makeContainer(gameId,
                                               plugId, 
                                               cusPlugId,
                                               ruleTemplId,
                                               null,
                                               element.getRule(),
                                               element.isActivated());
        
        return super.createResource(gameRule, builder);
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_PATH, method = RequestMethod.GET)
    public @ResponseBody GameRuleCollectionResponse readGameRules(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId) {
        
        //TODO: validate the ids
        //TODO: this service will provide a list of rules related to a game 
        //(activated or deactivated, this is not important), to a ruleTemplate
        //and to a customizedPlugin (ruleTemplate and customizedPlugin are
        //inside the rule)
        
        GameRule gameRule = this.makeContainer(gameId,
                                               plugId, 
                                               cusPlugId,
                                               ruleTemplId,
                                               null,
                                               null,
                                               null);
        
        Collection<GameRule> results = super.readResources(gameRule);
                                                
        GameRuleCollectionResponse response = new GameRuleCollectionResponse();
        response.setGameRules(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody GameRuleResponse readGameRuleById(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM) Integer ruleId) {
        
        //TODO: validate the ids
        
        GameRule gameRule = this.makeContainer(gameId,
                                               plugId, 
                                               cusPlugId,
                                               ruleTemplId,
                                               ruleId,
                                               null,
                                               null);
        
        GameRule result = super.readResourceById(gameRule);
        
        GameRuleResponse response = new GameRuleResponse();
        response.setGameRule(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateGameRule(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM) Integer ruleId,
                         @RequestBody GameRule element,
                         UriComponentsBuilder builder) {
        
        //TODO: validate the ids
        //TODO: update the new rule provided and update an instance in gameRule
        //in order to update the game indicated with the rule provided
        //(useful to set activated or deactivated)
        
        GameRule gameRule = this.makeContainer(gameId,
                                               plugId, 
                                               cusPlugId,
                                               ruleTemplId,
                                               ruleId,
                                               element.getRule(),
                                               element.isActivated());
        
        return super.updateResource(gameRule, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteGameRule(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM) Integer ruleTemplId,
                         @PathVariable(value = IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM) Integer ruleId) {
        
        //TODO: validate the ids
        
        GameRule gameRule = this.makeContainer(gameId,
                                               plugId, 
                                               cusPlugId,
                                               ruleTemplId,
                                               ruleId,
                                               null,
                                               null);
        
        return super.deleteResource(gameRule);
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(GameRule containerWithIds, 
                                                       GameRule result, 
                                                       Map<String, Object> uriVariables) {
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, result.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, result.getRule().getRuleTemplate().getPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, result.getRule().getCustomizedPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM, result.getRule().getRuleTemplate().getId());
        uriVariables.put(IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_SINGLE_PATH_PARAM, result.getRule().getId());
        
        return uriVariables;
    }
    
    
    protected GameRule makeContainer(Integer gameId,
                                     Integer plugId,
                                     Integer cusPlugId, 
                                     Integer ruleTemplId, 
                                     Integer ruleId,
                                     Rule rule,
                                     Boolean activated) {
        
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
        
        GameRule element = new GameRule();
        element.setGame(game);
        element.setRule(rule);
        element.setActivated(activated);
        
        return element;
    }
    
    
    @Qualifier("mockGameRuleManager")
    @Autowired
    public void setManager(IRestCrudManager<GameRule, GameRule, GameRule> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<GameRule, GameRule, GameRule> restCrudHelper) {
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