package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.response.ApplicationCollectionResponse;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.utils.rest.ICrudManager;
import eu.trentorise.utils.rest.AbstractRestCrudController;
import eu.trentorise.utils.rest.RestCrudHelper;
import eu.trentorise.utils.rest.RestResultHelper;
import java.util.Collection;
import java.util.Map;
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
public class ApplicationController extends AbstractRestCrudController<Application, Object, Application> {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public ApplicationController() {
        super(IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH,
              LoggerFactory.getLogger(ApplicationController.class.getName()));
    }
    

    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_APPLICATIONS_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createApplication(@RequestBody Application application,
                                                  UriComponentsBuilder builder) {
        
        return super.createResource(application, builder);
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_APPLICATIONS_PATH, method = RequestMethod.GET)
    public @ResponseBody ApplicationCollectionResponse readApplications() {
        Collection<Application> results = super.readResources(null);
                                                
        ApplicationCollectionResponse response = new ApplicationCollectionResponse();
        response.setApplications(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody ApplicationResponse readApplicationById(@PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId) {
        
        Application app = new Application();
        app.setId(appId);
        
        Application result = super.readResourceById(app);
        
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
        
        return super.updateResource(application, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteApplication(@PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId) {
        
        Application app = new Application();
        app.setId(appId);
        
        return super.deleteResource(app);
    }
    
    
    @Override
    protected Map<String, Object> populateUriVariables(Application containerWithIds,
                                                       Application result, 
                                                       Map<String, Object> uriVariables) {
        
        uriVariables.put(IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    

    @Qualifier("mockApplicationManager")
    @Autowired
    public void setManager(ICrudManager<Application, Object, Application> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<Application, Object, Application> restCrudHelper) {
        this.restCrudHelper = restCrudHelper;
    }

    @Qualifier("restResultHelper")
    @Autowired
    public void setRestResultHelper(RestResultHelper restResultHelper) {
        this.restResultHelper = restResultHelper;
    }
}