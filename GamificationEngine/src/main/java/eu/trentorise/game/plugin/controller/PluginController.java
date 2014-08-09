package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.container.IPluginContainer;
import eu.trentorise.game.plugin.container.PluginContainer;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.PluginCollectionResponse;
import eu.trentorise.game.plugin.response.PluginResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("pluginController")
public class PluginController extends AbstractCrudRestController<Plugin, Object, IPluginContainer> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public PluginController() {
        super(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH,
              LoggerFactory.getLogger(PluginController.class.getName()));
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_PLUGINS_PATH, method = RequestMethod.GET)
    public @ResponseBody PluginCollectionResponse readPlugins() {
        Collection<Plugin> results = super.readResources(null);
                                                
        PluginCollectionResponse response = new PluginCollectionResponse();
        response.setPlugins(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody PluginResponse readPluginById(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM) Integer plugId) {
        
        Plugin element = new Plugin();
        element.setId(plugId);
        
        IPluginContainer container = new PluginContainer();
        container.setGamificationPlugin(element);
        
        Plugin result = super.readResourceById(container);
        
        PluginResponse response = new PluginResponse();
        response.setPlugin(result);
        
        return response;
    }
        
    
    @Override
    protected Map<String, Object> populateUriVariables(IPluginContainer containerWithIds, 
                                                       Plugin result, Map<String, Object> uriVariables) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    @Qualifier("mockPluginManager")
    @Autowired
    public void setManager(IRestCrudManager<Plugin, Object, IPluginContainer> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<Plugin, Object, IPluginContainer> restCrudHelper) {
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