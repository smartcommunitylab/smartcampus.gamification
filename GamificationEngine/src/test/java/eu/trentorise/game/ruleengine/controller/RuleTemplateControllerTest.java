package eu.trentorise.game.ruleengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.service.MockActionManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.ruleengine.model.HandSideType;
import eu.trentorise.game.ruleengine.model.Operator;
import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.request.OperatorRequest;
import eu.trentorise.game.ruleengine.request.PluginOperatorRequest;
import eu.trentorise.game.ruleengine.request.RuleRequest;
import eu.trentorise.game.ruleengine.request.RuleTemplateRequest;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleSettingResponse;
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
public class RuleTemplateControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_RULEENGINE_RULETEMPLATE_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public RuleTemplateControllerTest() {
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
        
        //TODO: change restTemplate in order not to execute all this part even
        //though it is not necessary (when this kinds of tests are not 
        //activated)
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
     * Test of testGetOperatorsSupported method, of class RuleTemplateController.
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
        
        this.evaluateResponse(response, expectedElements);
    }
    
    /**
     * Test of testGetPluginOperatorsSupported method, of class RuleTemplateController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetPluginOperatorsSupported() throws Exception {
        MockRuleTemplateManager mock = new MockRuleTemplateManager();
        MockGamePluginManager gamePluginManagerMock = new MockGamePluginManager();
        
        this.executeTestGetPluginOperatorsSupported(gamePluginManagerMock.createPointsPlugin(), 
                                                    mock.createPointPluginOperators());
    }
    
    protected void executeTestGetPluginOperatorsSupported(GamificationPlugin plugin,
                                                          List<Operator> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<OperatorResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        PluginOperatorRequest request = new PluginOperatorRequest();
        request.setGamificationPlugin(plugin);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        OperatorResponse response = helper.executeTest("testGetPluginOperatorsSupported",
                                                       BASE_RELATIVE_URL + "/getPluginOperatorsSupported" + FINAL_PART_RELATIVE_URL,
                                                       OperatorResponse.class, 
                                                       jsonRequest);
        
        this.evaluateResponse(response, expectedElements);
    }

    protected void evaluateResponse(OperatorResponse response,
                                    List<Operator> expectedElements) {
        
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
    
    /**
     * Test of testSetRule method, of class RuleTemplateController.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetRule() throws Exception {
        MockRuleTemplateManager mock = new MockRuleTemplateManager();
        MockGamePluginManager gamePluginManagerMock = new MockGamePluginManager();
        
        Game game = new Game();
        game.setId(135);
        
        
        //TODO: to be completed
        this.executeTestSetRule(game, null);
    }
    
    protected void executeTestSetRule(Game game, Rule rule) throws Exception {
        
        RestTemplateJsonServiceTestHelper<RuleSettingResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        RuleRequest request = new RuleRequest();
        request.setGame(game);
        request.setRule(rule);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        RuleSettingResponse response = helper.executeTest("testSetRule",
                                                   BASE_RELATIVE_URL + "/setRule" + FINAL_PART_RELATIVE_URL,
                                                   RuleSettingResponse.class, 
                                                   jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
        }
    }
}