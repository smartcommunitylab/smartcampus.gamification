package eu.trentorise.game.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.request.BasicParamRequest;
import eu.trentorise.game.action.response.OperatorsResponse;
import eu.trentorise.game.action.service.MockActionManager;
import eu.trentorise.game.action.service.MockParamManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class ParamControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_ACTION_PARAM_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public ParamControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    
    /**
     * Test of testGetOperatorsSupported method, of class ParamController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOperatorsSupported() throws Exception {
        MockActionManager mock = new MockActionManager();
        MockParamManager paramManagerMock = new MockParamManager();
        
        
        BasicParam param = mock.createBikeKmParam();
        List<String> expectedElements = paramManagerMock.createElements();
        this.executeTest(param, expectedElements);
    }
    
    protected void executeTest(BasicParam param, 
                               List<String> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<OperatorsResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        BasicParamRequest request = new BasicParamRequest();
        request.setParam(param);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        OperatorsResponse response = helper.executeTest("testGetOperatorsSupported",
                                                        BASE_RELATIVE_URL + "/getOperatorsSupported" + FINAL_PART_RELATIVE_URL,
                                                        OperatorsResponse.class, 
                                                        jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<String> responseElements = response.getOperators();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                String responseElement = responseElements.get(i);
                String expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement, expectedElement);
            }
        }
    }
}