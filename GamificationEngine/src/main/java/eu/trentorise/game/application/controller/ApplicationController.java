package eu.trentorise.game.application.controller;

import eu.trentorise.game.application.service.IApplicationManager;
import eu.trentorise.game.application.container.ActionContainer;
import eu.trentorise.game.application.request.ActionRequest;
import eu.trentorise.game.application.container.IActionContainer;
import eu.trentorise.game.application.response.ActionResponse;
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
@Controller("applicationController")
@RequestMapping(IGameConstants.SERVICE_APPLICATION_PATH)
public class ApplicationController {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.POST, value = "/getActions" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody ActionResponse getActions(@RequestBody ActionRequest request) throws Exception {
        //TODO: this service will provide a list of gamifiableActions related to
        //the application specified in the request
        IActionContainer container = new ActionContainer();
        container.setAction(request.getAction());
        
        return manager.getActions(container);
    }
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected IApplicationManager manager;
}