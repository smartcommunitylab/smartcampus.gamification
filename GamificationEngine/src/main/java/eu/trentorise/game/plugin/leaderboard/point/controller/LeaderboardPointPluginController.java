package eu.trentorise.game.plugin.leaderboard.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;
import eu.trentorise.game.plugin.leaderboard.point.response.LeaderboardPointPluginCollectionResponse;
import eu.trentorise.game.plugin.leaderboard.point.response.LeaderboardPointPluginResponse;
import eu.trentorise.game.plugin.model.Plugin;
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
@Controller("leaderboardPointPluginController")
public class LeaderboardPointPluginController extends AbstractCrudRestController<LeaderboardPointPlugin, Object, LeaderboardPointPlugin> {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public LeaderboardPointPluginController() {
        super(IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH,
              LoggerFactory.getLogger(LeaderboardPointPluginController.class.getName()));
    }
    
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createLeaderboardPointPlugin(@RequestBody LeaderboardPointPlugin element,
                                                             UriComponentsBuilder builder) {
        
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        //TODO: this plugin knows what is the id of the main related plugin,
        //think how to implement it
        Plugin plugin = this.makePlugin();
        element.setPlugin(plugin);
        
        return super.createResource(element, builder);
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_PATH, method = RequestMethod.GET)
    public @ResponseBody LeaderboardPointPluginCollectionResponse readLeaderboardPointPlugins() {
        Collection<LeaderboardPointPlugin> results = super.readResources(null);
                                                
        LeaderboardPointPluginCollectionResponse response = new LeaderboardPointPluginCollectionResponse();
        response.setLeaderboardPointPlugins(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody LeaderboardPointPluginResponse readLeaderboardPointPluginById(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH_PARAM) Integer cusPlugId) {
        
        Plugin plugin = this.makePlugin();
        
        LeaderboardPointPlugin element = new LeaderboardPointPlugin();
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        LeaderboardPointPlugin result = super.readResourceById(element);
        
        LeaderboardPointPluginResponse response = new LeaderboardPointPluginResponse();
        response.setLeaderboardPointPlugin(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateLeaderboardPointPlugin(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH_PARAM) Integer cusPlugId, 
                                                                           @RequestBody LeaderboardPointPlugin element,
                                                                           UriComponentsBuilder builder) {
        
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        Plugin plugin = this.makePlugin();
        
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        return super.updateResource(element, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteLeaderboardPointPlugin(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH_PARAM) Integer cusPlugId) {
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        
        Plugin plugin = this.makePlugin();
        
        LeaderboardPointPlugin element = new LeaderboardPointPlugin();
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        return super.deleteResource(element);
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(LeaderboardPointPlugin containerWithIds, LeaderboardPointPlugin result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
    protected Plugin makePlugin() {
        Plugin plugin = new Plugin();
        plugin.setId(IGameConstants.SEQUENCE_INITIAL_VALUE + 2);
        return plugin;
    }

    
    @Qualifier("mockLeaderboardPointPluginManager")
    @Autowired
    public void setManager(IRestCrudManager<LeaderboardPointPlugin, Object, LeaderboardPointPlugin> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<LeaderboardPointPlugin, Object, LeaderboardPointPlugin> restCrudHelper) {
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