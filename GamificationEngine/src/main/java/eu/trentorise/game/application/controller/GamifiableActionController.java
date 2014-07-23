package eu.trentorise.game.application.controller;

import eu.trentorise.game.application.service.IGamifiableActionManager;
import eu.trentorise.game.application.container.ExternalActionContainer;
import eu.trentorise.game.application.request.ExternalActionRequest;
import eu.trentorise.game.application.container.IExternalActionContainer;
import eu.trentorise.game.application.response.ParamResponse;
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
@Controller("gamifiableActionController")
@RequestMapping(IGameConstants.SERVICE_APPLICATION_ACTION_PATH)
public class GamifiableActionController {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.POST, value = "/getParams" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody ParamResponse getParams(@RequestBody ExternalActionRequest request) throws Exception {
        //TODO: this service will provide a list of params related to
        //the action specified in the request
        IExternalActionContainer container = new ExternalActionContainer();
        container.setAction(request.getAction());
        
        return manager.getParams(container);
    }
    
    @Qualifier("mockGamifiableActionManager")
    @Autowired
    protected IGamifiableActionManager manager;
}