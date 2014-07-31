package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.container.ActionContainer;
import eu.trentorise.game.action.container.ExternalActionContainer;
import eu.trentorise.game.action.container.IActionContainer;
import eu.trentorise.game.action.container.IExternalActionContainer;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.request.ActionRequest;
import eu.trentorise.game.action.request.ExternalActionRequest;
import eu.trentorise.game.action.response.ExternalActionResponse;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.action.service.IActionManager;
import eu.trentorise.game.controller.IGameConstants;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping(IGameConstants.SERVICE_APPLICATIONS_PATH)
public class ApplicationController {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Application> get() throws Exception {
        List<Application> elements = new ArrayList<>();
        
        Application application = new Application();
        application.setId(1);
        elements.add(application);
        
        application = new Application();
        application.setId(2);
        elements.add(application);
        
        application = new Application();
        application.setId(3);
        elements.add(application);
        
        return elements;
        
        //return manager.getExternalActions(container);
    }
    
    /*@RequestMapping(method = RequestMethod.POST, value = "/getActionParams" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody ParamResponse getActionParams(@RequestBody ActionRequest request) throws Exception {
        //TODO: this service will provide a list of params related to
        //the action specified in the request
        IActionContainer container = new ActionContainer();
        container.setAction(request.getAction());
        
        return manager.getActionParams(container);
    }*/
    
    @Qualifier("mockActionManager")
    @Autowired
    protected IActionManager manager;
}