package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.GameCustomizedPluginCollectionResponse;
import eu.trentorise.game.plugin.response.GameCustomizedPluginResponse;
import eu.trentorise.game.profile.game.model.Game;
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
@Controller("gameCustomizedPluginController")
public class GameCustomizedPluginController extends AbstractCrudRestController<GameCustomizedPlugin, 
                                                                               GameCustomizedPlugin, 
                                                                               GameCustomizedPlugin> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public GameCustomizedPluginController() {
        super(IGameConstants.SERVICE_GAMECUSTOMIZEDPLUGINS_SINGLE_PATH,
              LoggerFactory.getLogger(GameCustomizedPluginController.class.getName()));
    }
    
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_GAMECUSTOMIZEDPLUGINS_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createGameCustomizedPlugin(
                                @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                                @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                                @RequestBody GameCustomizedPlugin element,
                                UriComponentsBuilder builder) {
        
        //TODO: validate the ids
        GameCustomizedPlugin gcp = this.makeGameCustomizedPlugin(gameId, plugId,
                                                                 element.getCustomizedPlugin().getId(),
                                                                 element.isActivated());
        
        return super.createResource(gcp, builder);
    }
    
    
    //READ
    @RequestMapping(value = IGameConstants.SERVICE_GAMECUSTOMIZEDPLUGINS_PATH, method = RequestMethod.GET)
    public @ResponseBody GameCustomizedPluginCollectionResponse readGameCustomizedPlugins(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId) {
        
        GameCustomizedPlugin gcp = this.makeGameCustomizedPlugin(gameId, plugId, 
                                                                 null, null);
        
        Collection<GameCustomizedPlugin> results = super.readResources(gcp);
                                                
        GameCustomizedPluginCollectionResponse response = new GameCustomizedPluginCollectionResponse();
        response.setGameCustomizedPlugins(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_GAMECUSTOMIZEDPLUGINS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody GameCustomizedPluginResponse readGameCustomizedPluginById(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId) {
        
        GameCustomizedPlugin gcp = this.makeGameCustomizedPlugin(gameId, plugId, 
                                                                 cusPlugId, null);
        
        GameCustomizedPlugin result = super.readResourceById(gcp);
        
        GameCustomizedPluginResponse response = new GameCustomizedPluginResponse();
        response.setGameCustomizedPlugin(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_GAMECUSTOMIZEDPLUGINS_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateGameCustomizedPlugin(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId, 
                         @RequestBody GameCustomizedPlugin element,
                         UriComponentsBuilder builder) {
        
        //TODO: validate the ids
        GameCustomizedPlugin gcp = this.makeGameCustomizedPlugin(gameId, plugId, 
                                                                 cusPlugId,
                                                                 element.isActivated());
        
        return super.updateResource(gcp, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_GAMECUSTOMIZEDPLUGINS_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteGameCustomizedPlugin(@PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId) {
        
        //TODO: validate the ids
        GameCustomizedPlugin gcp = this.makeGameCustomizedPlugin(gameId, plugId, 
                                                                 cusPlugId, null);
        
        return super.deleteResource(gcp);
    }
    
    
    @Override
    protected Map<String, Object> populateUriVariables(GameCustomizedPlugin containerWithIds, 
                                                       GameCustomizedPlugin result, 
                                                       Map<String, Object> uriVariables) {
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, result.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, result.getCustomizedPlugin().getPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, result.getCustomizedPlugin().getId());
        
        return uriVariables;
    }
    
    
    protected GameCustomizedPlugin makeGameCustomizedPlugin(Integer gameId, 
                                                            Integer plugId,
                                                            Integer cusPlugId,
                                                            Boolean activated) {
        
        Game game = new Game();
        game.setId(gameId);
        
        Plugin plugin = new Plugin();
        plugin.setId(plugId);
        
        CustomizedPlugin customizedPlugin = new CustomizedPlugin();
        customizedPlugin.setId(cusPlugId);
        customizedPlugin.setPlugin(plugin);
        
        GameCustomizedPlugin gcp = new GameCustomizedPlugin();
        gcp.setGame(game);
        gcp.setCustomizedPlugin(customizedPlugin);
        gcp.setActivated(activated);
        
        return gcp;
    }
    
    
    @Qualifier("mockGameCustomizedPluginManager")
    @Autowired
    public void setManager(IRestCrudManager<GameCustomizedPlugin, GameCustomizedPlugin, GameCustomizedPlugin> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<GameCustomizedPlugin, GameCustomizedPlugin, GameCustomizedPlugin> restCrudHelper) {
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