package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.response.ApplicationCollectionResponse;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.application.service.IApplicationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.utils.rest.RestResponseHelper;
import java.util.Collection;
import org.slf4j.Logger;
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
@Controller("applicationController")
@RequestMapping(IGameConstants.SERVICE_APPLICATIONS_PATH)
public class ApplicationController {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class.getName());
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ApplicationCollectionResponse findApplications() {
        Collection<Application> result = null;
        Exception exception = null;
        try {
            result = manager.findApplications();
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResponseHelper.handleFindingResult(result, exception, logger);
        }
        
        ApplicationCollectionResponse response = new ApplicationCollectionResponse();
        response.setApplications(result);
        
        return response;
    }
    
    @RequestMapping(value = "/{appId}", method = RequestMethod.GET)
    public @ResponseBody ApplicationResponse findApplicationById(@PathVariable Integer appId) {
        Application result = null;
        Exception exception = null;
        try {
            result = manager.findApplicationById(appId);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            restResponseHelper.handleFindingResult(result, exception, logger);
        }
        
        ApplicationResponse response = new ApplicationResponse();
        response.setApplication(result);
        
        return response;
    }
    
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected IApplicationManager manager;
    
    
    @Qualifier("restResponseHelper")
    @Autowired
    protected RestResponseHelper restResponseHelper;
}