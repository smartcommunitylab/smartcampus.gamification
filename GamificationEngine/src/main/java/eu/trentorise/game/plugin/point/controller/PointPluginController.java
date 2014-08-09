package eu.trentorise.game.plugin.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.PluginIdentifier;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.point.response.PointPluginCollectionResponse;
import eu.trentorise.game.plugin.point.response.PointPluginResponse;
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
@Controller("pointPluginController")
public class PointPluginController extends AbstractCrudRestController<PointPlugin, Object, PointPlugin> {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public PointPluginController() {
        super(IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH,
              LoggerFactory.getLogger(PointPluginController.class.getName()));
    }
    
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_POINT_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createPointPlugin(@RequestBody PointPlugin element,
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
    @RequestMapping(value=IGameConstants.SERVICE_PLUGINS_POINT_PATH, method = RequestMethod.GET)
    public @ResponseBody PointPluginCollectionResponse readPointPlugins() {
        Collection<PointPlugin> results = super.readResources(null);
                                                
        PointPluginCollectionResponse response = new PointPluginCollectionResponse();
        response.setPointPlugins(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody PointPluginResponse readPointPluginById(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH_PARAM) Integer cusPlugId) {
        
        Plugin plugin = this.makePlugin();
        
        PointPlugin element = new PointPlugin();
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        PointPlugin result = super.readResourceById(element);
        
        PointPluginResponse response = new PointPluginResponse();
        response.setPointPlugin(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updatePointPlugin(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH_PARAM) Integer cusPlugId, 
                                                                @RequestBody PointPlugin element,
                                                                UriComponentsBuilder builder) {
        
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        Plugin plugin = this.makePlugin();
        
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        return super.updateResource(element, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deletePointPlugin(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH_PARAM) Integer cusPlugId) {
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        
        Plugin plugin = this.makePlugin();
        
        PointPlugin element = new PointPlugin();
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        return super.deleteResource(element);
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(PointPlugin containerWithIds, PointPlugin result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
    protected Plugin makePlugin() {
        Plugin plugin = new Plugin();
        plugin.setId(PluginIdentifier.POINT_PLUGIN.ordinal());
        return plugin;
    }

    
    @Qualifier("mockPointPluginManager")
    @Autowired
    public void setManager(IRestCrudManager<PointPlugin, Object, PointPlugin> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<PointPlugin, Object, PointPlugin> restCrudHelper) {
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