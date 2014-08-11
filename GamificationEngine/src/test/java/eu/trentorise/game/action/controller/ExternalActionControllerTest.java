package eu.trentorise.game.action.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.request.ExternalActionCollectionCreationRequest;
import eu.trentorise.game.action.response.ExternalActionCollectionResponse;
import eu.trentorise.game.action.service.MockExternalActionManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
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
public class ExternalActionControllerTest extends SkipServiceTestHelper {

    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_EXTERNALACTIONS_PATH;
    
    protected static final MockExternalActionManager mockExternalActionManager = MockExternalActionManager.createInstance();
    
    public ExternalActionControllerTest() {
        super("ExternalActionControllerTest");
    }
    
    
    //CREATE CUSTOM - IMPORT GAMIFIABLE (EXTERNAL) ACTIONS
    @Test
    public void testCreateExternalActions() throws Exception {
        ExternalActionCollectionCreationRequest request = new ExternalActionCollectionCreationRequest();
        request.setFileContent("fileEncodedWithBase64");
        Collection<ExternalAction> expectedElements = mockExternalActionManager.createElements(null);
        this.executeTestCreateExternalActions(request, 
                                              (List<ExternalAction>) expectedElements,
                                              HttpStatus.OK,
                                              makeBaseRelativeUrlExpanded(null));
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
    
    
    protected String makeBaseRelativeUrlExpanded(Integer appId) {
        if (null == appId) {
            appId = 0;
        }
        
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_APPLICATIONS_SINGLE_PATH_PARAM, appId);
        
        IUrlMaker urlMaker = new UrlMaker();
        return urlMaker.makeUrl(BASE_RELATIVE_URL, uriVariables);
    }
}