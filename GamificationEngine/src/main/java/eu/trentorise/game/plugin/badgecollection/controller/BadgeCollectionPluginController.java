package eu.trentorise.game.plugin.badgecollection.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionPluginCollectionResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionPluginResponse;
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
@Controller("badgeCollectionPluginController")
public class BadgeCollectionPluginController extends AbstractCrudRestController<BadgeCollectionPlugin, Object, BadgeCollectionPlugin> {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public BadgeCollectionPluginController() {
        super(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH,
              LoggerFactory.getLogger(BadgeCollectionPluginController.class.getName()));
    }
    
    /*@RequestMapping(method = RequestMethod.POST, value = "/setCustomizedGamificationPlugin" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody SettingCustomizedPluginResponse setCustomizedGamificationPlugin(@RequestBody BadgeCollectionPluginRequest request) {
        return super.setCustomizedGamificationPlugin((AbstractCustomizedPluginRequest) request);
    }*/
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createBadgeCollection(@RequestBody BadgeCollectionPlugin element,
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
    @RequestMapping(value=IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_PATH, method = RequestMethod.GET)
    public @ResponseBody BadgeCollectionPluginCollectionResponse readBadgeCollections() {
        Collection<BadgeCollectionPlugin> results = super.readResources(null);
                                                
        BadgeCollectionPluginCollectionResponse response = new BadgeCollectionPluginCollectionResponse();
        response.setBadgeCollectionPlugins(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody BadgeCollectionPluginResponse readBadgeCollectionById(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId) {
        
        Plugin plugin = this.makePlugin();
        
        BadgeCollectionPlugin element = new BadgeCollectionPlugin();
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        BadgeCollectionPlugin result = super.readResourceById(element);
        
        BadgeCollectionPluginResponse response = new BadgeCollectionPluginResponse();
        response.setBadgeCollectionPlugin(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateBadgeCollection(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId, 
                                                                    @RequestBody BadgeCollectionPlugin element,
                                                                    UriComponentsBuilder builder) {
        
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        Plugin plugin = this.makePlugin();
        
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        return super.updateResource(element, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteBadgeCollection(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId) {
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        
        Plugin plugin = this.makePlugin();
        
        BadgeCollectionPlugin element = new BadgeCollectionPlugin();
        element.setId(cusPlugId);
        element.setPlugin(plugin);
        
        return super.deleteResource(element);
    }
    
    
    @Override
    protected Map<String, Object> populateUriVariables(BadgeCollectionPlugin containerWithIds,
                                                       BadgeCollectionPlugin result,
                                                       Map<String, Object> uriVariables) {
        
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
    protected Plugin makePlugin() {
        Plugin plugin = new Plugin();
        plugin.setId(IGameConstants.SEQUENCE_INITIAL_VALUE + 1);
        return plugin;
    }
    
    
    @Qualifier("mockBadgeCollectionPluginManager")
    @Autowired
    public void setManager(IRestCrudManager<BadgeCollectionPlugin, Object, BadgeCollectionPlugin> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<BadgeCollectionPlugin, Object, BadgeCollectionPlugin> restCrudHelper) {
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