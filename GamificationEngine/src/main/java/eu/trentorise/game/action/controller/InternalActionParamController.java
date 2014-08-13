package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.model.InternalAction;
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
@Controller("internalActionParamController")
public class InternalActionParamController extends AbstractCrudRestController<Param, InternalAction, Param> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public InternalActionParamController() {
        super(IGameConstants.SERVICE_INTERNALACTIONS_PARAMS_SINGLE_PATH, 
              LoggerFactory.getLogger(InternalActionParamController.class.getName()));
    }
        
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_INTERNALACTIONS_PARAMS_PATH, method = RequestMethod.GET)
    public @ResponseBody ParamCollectionResponse readInternalActionParams(
                         @PathVariable(value = IGameConstants.SERVICE_INTERNALACTIONS_SINGLE_PATH_PARAM) Integer intActId) {
        
        Collection<Param> results = super.readResources(this.makeInternalAction(intActId));
                                                
        ParamCollectionResponse response = new ParamCollectionResponse();
        response.setParams(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_INTERNALACTIONS_PARAMS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody ParamResponse readInternalActionParamById(
                         @PathVariable(value = IGameConstants.SERVICE_INTERNALACTIONS_SINGLE_PATH_PARAM) Integer intActId,
                         @PathVariable(value = IGameConstants.SERVICE_INTERNALACTIONS_PARAMS_SINGLE_PATH_PARAM) String paramName) {
        
        InternalAction externalAction = this.makeInternalAction(intActId);
               
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
        uriVariables.put(IGameConstants.SERVICE_INTERNALACTIONS_SINGLE_PATH_PARAM, result.getAction().getId());
        uriVariables.put(IGameConstants.SERVICE_INTERNALACTIONS_PARAMS_SINGLE_PATH_PARAM, result.getName());
        
        return uriVariables;
    }
    
    
    protected InternalAction makeInternalAction(Integer intActId) {        
        InternalAction element = new InternalAction();
        element.setId(intActId);
        
        return element;
    }
    
        
    @Qualifier("mockInternalActionParamManager")
    @Autowired
    public void setManager(IRestCrudManager<Param, InternalAction, Param> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<Param, InternalAction, Param> restCrudHelper) {
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