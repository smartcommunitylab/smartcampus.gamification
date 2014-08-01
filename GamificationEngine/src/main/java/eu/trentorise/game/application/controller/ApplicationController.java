package eu.trentorise.game.application.controller;

import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.application.service.IApplicationManager;
import eu.trentorise.game.controller.IGameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
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
    public @ResponseBody ApplicationResponse get() throws Exception {
        return manager.getApplications();
    }
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected IApplicationManager manager;
}