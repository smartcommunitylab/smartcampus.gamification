package eu.trentorise.game.ruleengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.service.MockActionManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.ruleengine.model.HandSideType;
import eu.trentorise.game.ruleengine.model.Operator;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.request.OperatorRequest;
import eu.trentorise.game.ruleengine.request.RuleTemplateRequest;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import eu.trentorise.game.ruleengine.service.MockRuleTemplateManager;
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
public class RuleEngineControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_RULEENGINE_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public RuleEngineControllerTest() {
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
     * Test of getRuleTemplates method, of class RuleEngineController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetRuleTemplates() throws Exception {
        MockGamePluginManager mock = new MockGamePluginManager();
        MockRuleTemplateManager ruleTemplateMock = new MockRuleTemplateManager();
        ruleTemplateMock.setManager(mock);
        
        
        GamificationPlugin plugin = mock.createPointsPlugin();
        List<RuleTemplate> expectedElements = ruleTemplateMock.createPointPluginRuleTemplateList();
        this.executeTestGetRuleTemplates(plugin, expectedElements);
        
        plugin = mock.createBadgeCollectionPlugin();
        expectedElements = ruleTemplateMock.createBadgeCollectionPluginRuleTemplateList();
        this.executeTestGetRuleTemplates(plugin, expectedElements);
        
        plugin = mock.createLeadearboardPointPlugin();
        expectedElements = ruleTemplateMock.createLeaderboardPointPluginRuleTemplateList();
        this.executeTestGetRuleTemplates(plugin, expectedElements);
    }
    
    protected void executeTestGetRuleTemplates(GamificationPlugin plugin,
                                               List<RuleTemplate> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<RuleTemplateResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        RuleTemplateRequest request = new RuleTemplateRequest();
        RuleTemplate ruleTemplate = new RuleTemplate();
        ruleTemplate.setPlugin(plugin);
        request.setRuleTemplate(ruleTemplate);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        RuleTemplateResponse response = helper.executeTest("testGetRuleTemplates",
                                                           BASE_RELATIVE_URL + "/getRuleTemplates" + FINAL_PART_RELATIVE_URL,
                                                           RuleTemplateResponse.class, 
                                                           jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<RuleTemplate> responseElements = response.getRuleTemplates();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                RuleTemplate responseElement = responseElements.get(i);
                RuleTemplate expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getId(), expectedElement.getId());
                assertEquals(responseElement.getPlugin().getId(), 
                             expectedElement.getPlugin().getId());
                assertEquals(responseElement.getName(), 
                             expectedElement.getName());
                assertEquals(responseElement.getType(), 
                             expectedElement.getType());
                assertEquals(responseElement.getDescription(), 
                             expectedElement.getDescription());
            }
        }
    }
    
    /**
     * Test of testGetOperatorsSupported method, of class RuleEngineController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOperatorsSupported() throws Exception {
        MockRuleTemplateManager mock = new MockRuleTemplateManager();
        MockActionManager actionManagerMock = new MockActionManager();
        
        BasicParam createBikeKmParam = actionManagerMock.createBikeKmParam();
        
        this.executeTestGetOperatorsSupported(createBikeKmParam,
                                              HandSideType.LEFT, 
                                              mock.createIntegerLeftHandSideOperators());
        
        this.executeTestGetOperatorsSupported(createBikeKmParam,
                                              HandSideType.RIGHT, 
                                              mock.createIntegerRightHandSideOperators());
    }
    
    protected void executeTestGetOperatorsSupported(BasicParam param,
                                                    HandSideType handSideType,
                                                    List<Operator> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<OperatorResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        OperatorRequest request = new OperatorRequest();
        request.setParam(param);
        request.setHandSideType(handSideType);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        OperatorResponse response = helper.executeTest("testGetOperatorsSupported",
                                                       BASE_RELATIVE_URL + "/getOperatorsSupported" + FINAL_PART_RELATIVE_URL,
                                                       OperatorResponse.class, 
                                                       jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<Operator> responseElements = response.getOperators();
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElements.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                Operator responseElement = responseElements.get(i);
                Operator expectedElement = expectedElements.get(i);
                
                assertEquals(responseElement.getSymbol(), expectedElement.getSymbol());
            }
        }
    }
}