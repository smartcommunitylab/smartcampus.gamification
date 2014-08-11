package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.container.IImportExternalActionContainer;
import eu.trentorise.game.action.container.ImportExternalActionContainer;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.request.ExternalActionCollectionCreationRequest;
import eu.trentorise.game.action.response.ExternalActionCollectionResponse;
import eu.trentorise.game.action.response.ExternalActionResponse;
import eu.trentorise.game.action.service.IExternalActionImporter;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.RestHelper;
import eu.trentorise.utils.rest.RestResponseHelper;
import eu.trentorise.utils.rest.RestResultHelper;
import eu.trentorise.utils.rest.crud.AbstractCrudRestController;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.RestCrudHelper;
import eu.trentorise.utils.rest.crud.RestCrudResponseHelper;
import eu.trentorise.utils.rest.crud.RestCrudResultHelper;
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


/**
 *
 * @author Luca Piras
 */
@Controller("externalActionController")
public class ExternalActionController extends AbstractCrudRestController<ExternalAction, Application, ExternalAction> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public ExternalActionController() {
        super(IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH, 
              LoggerFactory.getLogger(ExternalActionController.class.getName()));
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = IGameConstants.SERVICE_EXTERNALACTIONS_PATH)
    public @ResponseBody ExternalActionCollectionResponse createExternalActions(
                         @PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId,
                         @RequestBody ExternalActionCollectionCreationRequest request) throws Exception {
        
        //TODO: verify that appId is the same indicated into the file 
        
        ExternalActionCollectionResponse response = null;
        try {
            //decode base64 file
            String fileDecoded = this.decodeFile(request.getFileContent());
            IImportExternalActionContainer container = new ImportExternalActionContainer();
            container.setFileContent(fileDecoded);
        
            Collection<ExternalAction> results = (Collection<ExternalAction>) restHelper.execute(container, 
                                                                                                 externalActionImporter,
                                                                                                 "importExternalActions", 
                                                                                                 IImportExternalActionContainer.class,
                                                                                                 restResultHelper,
                                                                                                 logger);
            response = new ExternalActionCollectionResponse();
            response.setExternalActions(results);
        } catch (Exception ex) {
            customRestExceptionHandler.handleException(ex, "importExternalActions",
                                                       logger);
        }
        
        return response;
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_EXTERNALACTIONS_PATH, method = RequestMethod.GET)
    public @ResponseBody ExternalActionCollectionResponse readExternalActions(
                         @PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId) {
        
        Collection<ExternalAction> results = super.readResources(this.makeApplication(appId));
                                                
        ExternalActionCollectionResponse response = new ExternalActionCollectionResponse();
        response.setExternalActions(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody ExternalActionResponse readExternalActionById(
                         @PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId,
                         @PathVariable(value = IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM) Integer extActId) {
        
        Application app = this.makeApplication(appId);
               
        ExternalAction element = new ExternalAction();
        element.setId(extActId);
        element.setApplication(app);
        
        ExternalAction result = super.readResourceById(element);
        
        ExternalActionResponse response = new ExternalActionResponse();
        response.setExternalAction(result);
        
        return response;
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteExternalAction(
                         @PathVariable(value = IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM) Integer appId,
                         @PathVariable(value = IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM) Integer extActId) {
        
        Application app = this.makeApplication(appId);
        
        ExternalAction element = new ExternalAction();
        element.setId(extActId);
        element.setApplication(app);
        
        return super.deleteResource(element);
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(ExternalAction containerWithIds, ExternalAction result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM, result.getApplication().getId());
        uriVariables.put(IGameConstants.SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
   protected Application makeApplication(Integer appId) {
        Application element = new Application();
        element.setId(appId);
        return element;
    }
    
    protected String decodeFile(String fileContent) {
        //TODO: implement this part
        return fileContent;
    }
    
    //Beans for custom methods
    @Qualifier("mockExternalActionManager")
    @Autowired
    protected IExternalActionImporter externalActionImporter;
    
    
    @Qualifier("restHelper")
    @Autowired
    protected RestHelper restHelper;
    
    @Qualifier("restResultHelper")
    @Autowired
    protected RestResultHelper restResultHelper;
    
    @Qualifier("restResponseHelper")
    @Autowired
    protected RestResponseHelper restResponseHelper;
    
    @Qualifier("restExceptionHandler")
    @Autowired
    protected RestExceptionHandler customRestExceptionHandler;
    
    
    //beans for crud methods
    @Qualifier("mockExternalActionManager")
    @Autowired
    public void setManager(IRestCrudManager<ExternalAction, Application, ExternalAction> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<ExternalAction, Application, ExternalAction> restCrudHelper) {
        this.restCrudHelper = restCrudHelper;
    }

    @Qualifier("restCrudResultHelper")
    @Autowired
    public void setRestCrudResultHelper(RestCrudResultHelper restCrudResultHelper) {
        this.restCrudResultHelper = restCrudResultHelper;
    }
    
    @Qualifier("restCrudResponseHelper")
    @Autowired
    public void setRestCrudResponseHelper(RestCrudResponseHelper restCrudResponseHelper) {
        this.restCrudResponseHelper = restCrudResponseHelper;
    }
    
    @Qualifier("restExceptionHandler")
    @Autowired
    public void setRestExceptionHandler(RestExceptionHandler restExceptionHandler) {
        this.restExceptionHandler = restExceptionHandler;
    }
}