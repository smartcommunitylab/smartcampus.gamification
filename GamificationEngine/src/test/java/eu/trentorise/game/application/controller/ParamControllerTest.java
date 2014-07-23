package eu.trentorise.game.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.application.model.BasicParam;
import eu.trentorise.game.application.request.BasicParamRequest;
import eu.trentorise.game.application.response.OperatorsResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.application.service.MockGamifiableActionManager;
import eu.trentorise.game.application.service.MockParamManager;
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
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_APPLICATION_PARAM_PATH;
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
        MockApplicationManager mock = new MockApplicationManager();
        MockGamifiableActionManager gamifiableActionMock = new MockGamifiableActionManager();
        gamifiableActionMock.setManager(mock);
        MockParamManager paramManagerMock = new MockParamManager();
        
        
        BasicParam param = gamifiableActionMock.createBikeKmParam();
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