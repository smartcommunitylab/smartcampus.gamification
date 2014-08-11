package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.container.IImportExternalActionContainer;
import eu.trentorise.game.action.container.ImportExternalActionContainer;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.request.ExternalActionCollectionCreationRequest;
import eu.trentorise.game.action.response.ExternalActionCollectionResponse;
import eu.trentorise.game.action.service.IExternalActionImporter;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.RestHelper;
import eu.trentorise.utils.rest.RestResponseHelper;
import eu.trentorise.utils.rest.RestResultHelper;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ExternalActionController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalActionController.class.getName());
    
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
                                                                                                 manager,
                                                                                                 "importExternalActions", 
                                                                                                 IImportExternalActionContainer.class,
                                                                                                 restResultHelper,
                                                                                                 logger);
            response = new ExternalActionCollectionResponse();
            response.setExternalActions(results);
        } catch (Exception ex) {
            restExceptionHandler.handleException(ex, "importExternalActions",
                                                 logger);
        }
        
        return response;
    }
    
    
    protected String decodeFile(String fileContent) {
        //TODO: implement this part
        return fileContent;
    }
    
    @Qualifier("mockExternalActionManager")
    @Autowired
    protected IExternalActionImporter manager;
    
    
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
    protected RestExceptionHandler restExceptionHandler;
}