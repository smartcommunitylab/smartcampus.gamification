package eu.trentorise.game.plugin.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.PluginIdentifier;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.crud.AbstractCrudRestController;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.RestCrudHelper;
import eu.trentorise.utils.rest.crud.RestCrudResponseHelper;
import eu.trentorise.utils.rest.crud.RestCrudResultHelper;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public ResponseEntity<Void> createGame(@RequestBody PointPlugin element,
                                           UriComponentsBuilder builder) {
        
        //TODO: this plugin knows what is the id of the main related plugin,
        //think how to implement it
        Plugin plugin = new Plugin();
        plugin.setId(PluginIdentifier.POINT_PLUGIN.ordinal());
        element.setPlugin(plugin);
        
        return super.createResource(element, builder);
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(PointPlugin containerWithIds, PointPlugin result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_POINT_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
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