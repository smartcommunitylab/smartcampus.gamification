package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.response.ApplicationCollectionResponse;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.application.service.IApplicationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.utils.rest.AbstractRestResourceController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class ApplicationController extends AbstractRestResourceController<Application> {
    
    public ApplicationController() {
        super(LoggerFactory.getLogger(ApplicationController.class.getName()));
    }
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ApplicationCollectionResponse findApplications() {
        Collection<Application> result = super.findCollection(null);
        
        ApplicationCollectionResponse response = new ApplicationCollectionResponse();
        response.setApplications(result);
        
        return response;
    }
    
    @Override
    protected Collection<Application> serviceFindCollection(List<String> ids) throws Exception {
        return manager.findApplications();
    }
    
    @RequestMapping(value = "/{appId}", method = RequestMethod.GET)
    public @ResponseBody ApplicationResponse findApplicationById(@PathVariable Integer appId) {
        List<String> ids = new ArrayList<>();
        ids.add(appId.toString());
        
        Application result = super.findSingleElement(ids);
        
        ApplicationResponse response = new ApplicationResponse();
        response.setApplication(result);
        
        return response;
    }
    
    @Override
    protected Application serviceFindSingleElement(List<String> ids) throws Exception {
        return manager.findApplicationById(new Integer(ids.get(0)));
    }
    
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected IApplicationManager manager;
}