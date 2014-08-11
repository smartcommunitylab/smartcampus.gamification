package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.container.ActionContainer;
import eu.trentorise.game.action.container.IActionContainer;
import eu.trentorise.game.action.request.ActionRequest;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.action.service.IActionManager;
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
@Controller("actionController")
public class ActionController {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.POST, value = "/game/services/action/getActionParams" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody ParamResponse getActionParams(@RequestBody ActionRequest request) throws Exception {
        //TODO: this service will provide a list of params related to
        //the action specified in the request
        IActionContainer container = new ActionContainer();
        container.setAction(request.getAction());
        
        return manager.getActionParams(container);
    }
    
    @Qualifier("mockActionManager")
    @Autowired
    protected IActionManager manager;
}