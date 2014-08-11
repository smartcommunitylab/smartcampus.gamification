package eu.trentorise.game.action.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.Param;
import eu.trentorise.game.action.request.ActionRequest;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.action.service.MockActionManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public class ActionControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = "/game/services/action";
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    
    public ActionControllerTest() {
        super("ActionControllerTest");
    }
    
    
    @Test
    public void testGetActionParams() throws Exception {
        MockActionManager mock = MockActionManager.createInstance();
        
        Action action = mock.createExternalAction();
        List<Param> expectedElements = mock.createElements();
        this.executeTestGetActionParams(action, expectedElements);
    }
    
    protected void executeTestGetActionParams(Action action,
                               List<Param> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<ParamResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        ActionRequest request = new ActionRequest();
        request.setAction(action);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        ParamResponse response = helper.executeTest("testGetActionParams",
                                                    BASE_RELATIVE_URL + "/getActionParams" + FINAL_PART_RELATIVE_URL,
                                                    HttpMethod.POST,
                                                    ParamResponse.class, 
                                                    jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<Param> responseElements = response.getParams();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                Param responseElement = responseElements.get(i);
                Param expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getName(), expectedElement.getName());
                assertEquals(responseElement.getAction().getId(), 
                             expectedElement.getAction().getId());
                assertEquals(responseElement.getCompositeParam(), 
                             expectedElement.getCompositeParam());
            }
        }
    }
}