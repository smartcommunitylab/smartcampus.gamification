package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.model.Param;
import eu.trentorise.game.action.response.ParamCollectionResponse;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.controller.IGameConstants;
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
@Controller("externalActionParamController")
public class ExternalActionParamController extends AbstractCrudRestController<Param, ExternalAction, Param> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public ExternalActionParamController() {
        super(IGameConstants.SERVICE_EXTERNALACTIONS_PARAMS_SINGLE_PATH, 
              LoggerFactory.getLogger(ExternalActionParamController.class.getName()));
    }
        
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_EXTERNALACTIONS_PARAMS_PATH, method = RequestMethod.GET)
    public @ResponseBody ParamCollectionResponse readExternalActionParams(
                         @PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId,
                         @PathVariable(value = IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM) Integer extActId) {
        
        Collection<Param> results = super.readResources(this.makeExternalAction(appId, extActId));
                                                
        ParamCollectionResponse response = new ParamCollectionResponse();
        response.setParams(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_EXTERNALACTIONS_PARAMS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody ParamResponse readExternalActionParamById(
                         @PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId,
                         @PathVariable(value = IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM) Integer extActId,
                         @PathVariable(value = IGameConstants.SERVICE_EXTERNALACTIONS_PARAMS_SINGLE_PATH_PARAM) String paramName) {
        
        ExternalAction externalAction = this.makeExternalAction(appId, extActId);
               
        Param element = new Param();
        element.setName(paramName);
        element.setAction(externalAction);
        
        Param result = super.readResourceById(element);
        
        ParamResponse response = new ParamResponse();
        response.setParam(result);
        
        return response;
    }
    
    
    @Override
    protected Map<String, Object> populateUriVariables(Param containerWithIds, Param result, Map<String, Object> uriVariables) {
        ExternalAction element = (ExternalAction) result.getAction();
        uriVariables.put(IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM, element.getApplication().getId());
        uriVariables.put(IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM, element.getId());
        uriVariables.put(IGameConstants.SERVICE_EXTERNALACTIONS_PARAMS_SINGLE_PATH_PARAM, result.getName());
        
        return uriVariables;
    }
    
    
    protected ExternalAction makeExternalAction(Integer appId, 
                                                Integer extActId) {
        
        Application app = new Application();
        app.setId(appId);
        
        ExternalAction element = new ExternalAction();
        element.setId(extActId);
        element.setApplication(app);
        
        return element;
    }
    
        
    @Qualifier("mockExternalActionParamManager")
    @Autowired
    public void setManager(IRestCrudManager<Param, ExternalAction, Param> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<Param, ExternalAction, Param> restCrudHelper) {
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