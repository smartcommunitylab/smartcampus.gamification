package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.container.GameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.GameCustomizedPluginContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.CustomizedPluginCollectionResponse;
import eu.trentorise.game.plugin.response.CustomizedPluginResponse;
import eu.trentorise.game.profile.game.model.Game;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("gameCustomizedPluginController")
public class GameCustomizedPluginController extends AbstractCrudRestController<CustomizedPlugin, 
                                                                               IGameCustomizedPluginCollectionContainer, 
                                                                               IGameCustomizedPluginContainer> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public GameCustomizedPluginController() {
        super(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH,
              LoggerFactory.getLogger(GameCustomizedPluginController.class.getName()));
    }
    
    
    //READ
    @RequestMapping(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_PATH, method = RequestMethod.GET)
    public @ResponseBody CustomizedPluginCollectionResponse readGameCustomizedPlugins(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId) {
        
        GameCustomizedPlugin gcp = this.makeContainerContent(gameId, plugId, 
                                                             null);
        
        IGameCustomizedPluginCollectionContainer container = new GameCustomizedPluginCollectionContainer();
        container.setGameCustomizedPlugin(gcp);
        
        Collection<CustomizedPlugin> results = super.readResources(container);
                                                
        CustomizedPluginCollectionResponse response = new CustomizedPluginCollectionResponse();
        response.setCustomizedPlugins(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody CustomizedPluginResponse readGameCustomizedPluginById(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId,
                         @PathVariable(value = IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM) Integer cusPlugId) {
        
        GameCustomizedPlugin gcp = this.makeContainerContent(gameId, plugId, 
                                                             cusPlugId);
        
        IGameCustomizedPluginContainer container = new GameCustomizedPluginContainer();
        container.setGameCustomizedPlugin(gcp);
        
        CustomizedPlugin result = super.readResourceById(container);
        
        CustomizedPluginResponse response = new CustomizedPluginResponse();
        response.setCustomizedPlugin(result);
        
        return response;
    }
       
    @Override
    protected Map<String, Object> populateUriVariables(IGameCustomizedPluginContainer containerWithIds, 
                                                       CustomizedPlugin result, 
                                                       Map<String, Object> uriVariables) {
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, result.getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, result.getId());
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
    protected GameCustomizedPlugin makeContainerContent(Integer gameId, 
                                                        Integer plugId, 
                                                        Integer cusPlugId) {
        
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
        
        return gcp;
    }
    
    
    @Qualifier("mockGameCustomizedPluginManager")
    @Autowired
    public void setManager(IRestCrudManager<CustomizedPlugin, IGameCustomizedPluginCollectionContainer, IGameCustomizedPluginContainer> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<CustomizedPlugin, IGameCustomizedPluginCollectionContainer, IGameCustomizedPluginContainer> restCrudHelper) {
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
}