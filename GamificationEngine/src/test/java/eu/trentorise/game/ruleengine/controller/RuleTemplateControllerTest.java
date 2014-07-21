package eu.trentorise.game.ruleengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.rule.model.RuleTemplate;
import eu.trentorise.game.ruleengine.request.RuleTemplateRequest;
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
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_RULEENGINE_TEMPLATERULE_PATH;
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
     * Test of getRuleTemplateList method, of class RuleTemplateController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetRuleTemplateList() throws Exception {
        MockGamePluginManager mock = new MockGamePluginManager();
        MockRuleTemplateManager ruleTemplateMock = new MockRuleTemplateManager();
        ruleTemplateMock.setManager(mock);
        
        
        GamificationPlugin plugin = mock.createPointsPlugin();
        List<RuleTemplate> expectedElements = ruleTemplateMock.createPointPluginRuleTemplateList();
        this.executeTest(plugin, expectedElements);
        
        plugin = mock.createBadgeCollectionPlugin();
        expectedElements = ruleTemplateMock.createBadgeCollectionPluginRuleTemplateList();
        this.executeTest(plugin, expectedElements);
        
        plugin = mock.createLeadearboardPointPlugin();
        expectedElements = ruleTemplateMock.createLeaderboardPointPluginRuleTemplateList();
        this.executeTest(plugin, expectedElements);
    }
    
    protected void executeTest(GamificationPlugin plugin,
                               List<RuleTemplate> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<RuleTemplateResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        RuleTemplateRequest request = new RuleTemplateRequest();
        RuleTemplate ruleTemplate = new RuleTemplate();
        ruleTemplate.setPlugin(plugin);
        request.setRuleTemplate(ruleTemplate);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        RuleTemplateResponse response = helper.executeTest("testGetRuleTemplateList",
                                                           BASE_RELATIVE_URL + "/getRuleTemplateList" + FINAL_PART_RELATIVE_URL,
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
}