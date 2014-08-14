package eu.trentorise.game.ruleengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.service.MockExternalActionParamManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.ruleengine.model.HandSideType;
import eu.trentorise.game.ruleengine.model.Operator;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.request.OperatorRequest;
import eu.trentorise.game.ruleengine.request.PluginOperatorRequest;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateCollectionResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import eu.trentorise.game.ruleengine.service.MockRuleTemplateManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplateControllerTest extends AbstractRestCrudTest<RuleTemplate, 
                                                                     Plugin,
                                                                     RuleTemplate,
                                                                     RuleTemplateCollectionResponse,
                                                                     RuleTemplateResponse> {
    
    protected static final MockRuleTemplateManager mockRuleTemplateManager = MockRuleTemplateManager.createInstance();
    protected static final MockPluginManager mockPluginManager = MockPluginManager.createInstance();
    
    
    public RuleTemplateControllerTest() {
        super("RuleTemplateControllerTest", 
              IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_PATH,
              mockRuleTemplateManager,
              mockRuleTemplateManager.getComparator());
    }
    
    
    //CREATE
    /*@Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateRuleTemplate", null, 
                                makeBaseRelativeUrlExpanded(null));
    }*/
    
    @Override
    protected RuleTemplate manageElementToCreate(RuleTemplate element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        Plugin plugin = mockPluginManager.createPointsPlugin();
        super.testReadCollection("testReadRuleTemplates", 
                                 plugin, 
                                 RuleTemplateCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(plugin));
        
        plugin = mockPluginManager.createBadgeCollectionPlugin();
        super.testReadCollection("testReadRuleTemplates", 
                                 plugin, 
                                 RuleTemplateCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(plugin));
        
        plugin = mockPluginManager.createLeadearboardPointPlugin();
        super.testReadCollection("testReadRuleTemplates", 
                                 plugin, 
                                 RuleTemplateCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(plugin));
    }
    
    @Override
    protected List<RuleTemplate> retrieveCollection(RuleTemplateCollectionResponse response) {
        return (List<RuleTemplate>) response.getRuleTemplates();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        Plugin plugin = mockPluginManager.createPointsPlugin();
        super.testReadElementById("testReadRuleTemplateById", null, 
                                  RuleTemplateResponse.class,
                                  makeBaseRelativeUrlExpanded(plugin));
    }

    @Override
    protected RuleTemplate manageNegativeElementToReadById(RuleTemplate element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected RuleTemplate retrieveSingleElement(RuleTemplateResponse response) {
        return response.getRuleTemplate();
    }
    
    
    //UPDATE
    /*@Test
    public void testUpdateElement() throws Exception {
        Plugin plugin = mockPluginManager.createPointsPlugin();
        super.testUpdateElement("testUpdateRuleTemplate", null,
                                makeBaseRelativeUrlExpanded(plugin));
    }*/

    @Override
    protected RuleTemplate managePositiveElementToUpdate(RuleTemplate element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected RuleTemplate manageNegativeElementToUpdate(RuleTemplate element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    /*@Test
    public void testDeleteElement() throws Exception {
        Plugin plugin = mockPluginManager.createPointsPlugin();
        super.testDeleteElement("testDeleteRuleTemplate", null,
                                makeBaseRelativeUrlExpanded(plugin));
    }*/
    
    @Override
    protected RuleTemplate manageNegativeElementToDelete(RuleTemplate element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(RuleTemplate element) {
        return element.getId().toString();
    }

    
    protected RuleTemplate setNegativeId(RuleTemplate element) {
        element.setId(-1);
        
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(Plugin plugin) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, 
                         plugin.getId());
        
        return super.expandUrl(this.baseRelativeUrl, uriVariables);
    }
    
    
    /**
     * Test of testGetOperatorsSupported method, of class RuleTemplateController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOperatorsSupported() throws Exception {
        MockRuleTemplateManager mock = MockRuleTemplateManager.createInstance();
        MockExternalActionParamManager mockExternalActionParamManager = MockExternalActionParamManager.createInstance();
        
        BasicParam createBikeKmParam = mockExternalActionParamManager.createBikeKmParam();
        
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
                                                       "/game/services/ruleengine/ruletemplates" + "/getOperatorsSupported" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION,
                                                       HttpMethod.POST,
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
        MockPluginManager gamePluginManagerMock = new MockPluginManager();
        
        this.executeTestGetPluginOperatorsSupported(gamePluginManagerMock.createPointsPlugin(), 
                                                    mock.createPointPluginOperators());
    }
    
    protected void executeTestGetPluginOperatorsSupported(Plugin plugin,
                                                          List<Operator> expectedElements) throws Exception {
        
        RestTemplateJsonServiceTestHelper<OperatorResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        PluginOperatorRequest request = new PluginOperatorRequest();
        request.setGamificationPlugin(plugin);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        OperatorResponse response = helper.executeTest("testGetPluginOperatorsSupported",
                                                       "/game/services/ruleengine/ruletemplates" + "/getPluginOperatorsSupported" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION,
                                                       HttpMethod.POST,
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
}