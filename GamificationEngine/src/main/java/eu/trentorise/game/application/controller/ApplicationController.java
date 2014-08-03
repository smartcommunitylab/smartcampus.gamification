package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.response.ApplicationCollectionResponse;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.utils.rest.ICrudManager;
import eu.trentorise.utils.rest.RestCrudHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Luca Piras
 */
@Controller("applicationController")
public class ApplicationController {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class.getName());
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    //CREATE
    @RequestMapping(value=IGameConstants.SERVICE_APPLICATIONS_PATH, method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Void> createApplication(@RequestBody Application application, 
                                                                UriComponentsBuilder builder) {
        
        Application result = restCrudHelper.createSingleElement(application, 
                                                                manager,
                                                                logger);
    
        return restCrudHelper.makeCreationResponse(builder, 
                                                   IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH,
                                                   this.makeUriVariables(result));
        //return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_APPLICATIONS_PATH, method = RequestMethod.GET)
    public @ResponseBody ApplicationCollectionResponse readApplications() {
        Collection<Application> result = restCrudHelper.readCollection(null, 
                                                                           manager,
                                                                           logger);
                                                
        ApplicationCollectionResponse response = new ApplicationCollectionResponse();
        response.setApplications(result);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody ApplicationResponse readApplicationById(@PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId) {
        
        Application app = new Application();
        app.setId(appId);
        Application result = restCrudHelper.readSingleElement(app, 
                                                                  manager,
                                                                  logger);
        
        ApplicationResponse response = new ApplicationResponse();
        response.setApplication(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateApplication(@PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId, 
                                                                @RequestBody Application application,
                                                                UriComponentsBuilder builder) {
        
        application.setId(appId);
        
        Application result = restCrudHelper.updateSingleElement(application, 
                                                                manager,
                                                                logger);
    
        return restCrudHelper.makeUpdateResponse(builder, 
                                                 IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH,
                                                 this.makeUriVariables(result));
        //return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
    protected Map<String, Object> makeUriVariables(Application result) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put(IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM, result.getId());
        return uriVariables;
    }
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected ICrudManager<Application, Object, Application> manager;
    
    
    @Qualifier("applicationRestCrudHelper")
    @Autowired
    protected RestCrudHelper<Application, Object, Application> restCrudHelper;
}