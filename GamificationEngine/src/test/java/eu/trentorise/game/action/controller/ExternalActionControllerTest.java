package eu.trentorise.game.action.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.request.ExternalActionCollectionCreationRequest;
import eu.trentorise.game.action.response.ExternalActionCollectionResponse;
import eu.trentorise.game.action.response.ExternalActionResponse;
import eu.trentorise.game.action.service.MockExternalActionManager;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.utils.web.IUrlMaker;
import eu.trentorise.utils.web.UrlMaker;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Luca Piras
 */
public class ExternalActionControllerTest extends AbstractRestCrudTest<ExternalAction, 
                                                                       Application,
                                                                       ExternalAction,
                                                                       ExternalActionCollectionResponse,
                                                                       ExternalActionResponse> {

    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_EXTERNALACTIONS_PATH;
    
    protected static final MockExternalActionManager mockExternalActionManager = MockExternalActionManager.createInstance();
    protected static final MockApplicationManager mockApplicationManager = MockApplicationManager.createInstance();
    
    
    public ExternalActionControllerTest() {
        super("ExternalActionControllerTest", 
              IGameConstants.SERVICE_EXTERNALACTIONS_PATH,
              mockExternalActionManager,
              mockExternalActionManager.getComparator());
    }
    
    //CREATE - CUSTOM IMPORT GAMIFIABLE (EXTERNAL) ACTIONS
    @Test
    public void testCreateExternalActions() throws Exception {
        Application app = mockApplicationManager.createViaggiaRovereto();
        ExternalActionCollectionCreationRequest request = new ExternalActionCollectionCreationRequest();
        request.setFileContent("fileEncodedWithBase64");
        Collection<ExternalAction> expectedElements = mockExternalActionManager.createElements(null);
        this.executeTestCreateExternalActions(request, 
                                              (List<ExternalAction>) expectedElements,
                                              HttpStatus.OK,
                                              makeBaseRelativeUrlExpanded(app));
    }
    
    protected void executeTestCreateExternalActions(ExternalActionCollectionCreationRequest request,
                                                    Collection<ExternalAction> expectedElements,
                                                    HttpStatus expectedStatus,
                                                    String baseRelativeUrlExpanded) throws Exception {
        
        List<ExternalAction> expectedElementsList = (List<ExternalAction>) expectedElements;
        
        RestTemplateJsonServiceTestHelper<ExternalActionCollectionResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        ExternalActionCollectionResponse response = helper.executeTest("ExternalActionControllerTest - testCreateExternalActions",
                                                                       baseRelativeUrlExpanded,
                                                                       HttpMethod.POST,
                                                                       ExternalActionCollectionResponse.class, 
                                                                       jsonRequest,
                                                                       expectedStatus);
        
        if (null != response) {
            List<ExternalAction> responseElements = (List<ExternalAction>) response.getExternalActions();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                ExternalAction responseElement = responseElements.get(i);
                ExternalAction expectedElement = expectedElementsList.get(i);
                
                assertEquals(0, mockExternalActionManager.getComparator().compare(responseElement, expectedElement));
            }
        }
    }
    
    
    //CREATE
    /*@Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateExternalAction", null, 
                                makeBaseRelativeUrlExpanded(mockApplicationManager.createViaggiaRovereto()));
    }*/
    
    @Override
    protected ExternalAction manageElementToCreate(ExternalAction element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        Application app = mockApplicationManager.createViaggiaRovereto();
        super.testReadCollection("testReadExternalActions", 
                                 app, 
                                 ExternalActionCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(app));
    }
    
    @Override
    protected List<ExternalAction> retrieveCollection(ExternalActionCollectionResponse response) {
        return (List<ExternalAction>) response.getExternalActions();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadExternalActionById", null, 
                                  ExternalActionResponse.class,
                                  makeBaseRelativeUrlExpanded(mockApplicationManager.createViaggiaRovereto()));
    }

    @Override
    protected ExternalAction manageNegativeElementToReadById(ExternalAction element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected ExternalAction retrieveSingleElement(ExternalActionResponse response) {
        return response.getExternalAction();
    }
    
    
    //UPDATE
    /*@Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateExternalAction", null,
                                makeBaseRelativeUrlExpanded(mockApplicationManager.createViaggiaRovereto()));
    }*/

    @Override
    protected ExternalAction managePositiveElementToUpdate(ExternalAction element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected ExternalAction manageNegativeElementToUpdate(ExternalAction element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteExternalAction", null,
                                makeBaseRelativeUrlExpanded(mockApplicationManager.createViaggiaRovereto()));
    }
    
    @Override
    protected ExternalAction manageNegativeElementToDelete(ExternalAction element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(ExternalAction element) {
        return element.getId().toString();
    }

    
    protected ExternalAction setNegativeId(ExternalAction element) {
        element.setId(-1);
        
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(Application app) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM, 
                         app.getId());
        
        IUrlMaker urlMaker = new UrlMaker();
        return urlMaker.makeUrl(BASE_RELATIVE_URL, uriVariables);
    }
}