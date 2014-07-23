package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.service.IParamManager;
import eu.trentorise.game.action.container.BasicParamContainer;
import eu.trentorise.game.action.container.IBasicParamContainer;
import eu.trentorise.game.action.request.BasicParamRequest;
import eu.trentorise.game.action.response.OperatorsResponse;
import eu.trentorise.game.controller.IGameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("paramController")
@RequestMapping(IGameConstants.SERVICE_ACTION_PARAM_PATH)
public class ParamController {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.POST, value = "/getOperatorsSupported" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody OperatorsResponse getOperatorsSupported(@RequestBody BasicParamRequest request) throws Exception {
        //TODO: this service will provide a list of params related to
        //the action specified in the request
        IBasicParamContainer container = new BasicParamContainer();
        container.setParam(request.getParam());
        
        return manager.getOperatorsSupported(container);
    }
    
    @Qualifier("mockParamManager")
    @Autowired
    protected IParamManager manager;
}