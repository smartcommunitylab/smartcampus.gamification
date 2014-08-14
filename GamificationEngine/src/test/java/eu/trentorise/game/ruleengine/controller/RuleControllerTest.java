package eu.trentorise.game.ruleengine.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.ruleengine.container.IGameRuleContainer;
import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.response.RuleCollectionResponse;
import eu.trentorise.game.ruleengine.response.RuleResponse;
import eu.trentorise.game.ruleengine.service.MockRuleManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class RuleControllerTest extends AbstractRestCrudTest<Rule, 
                                                             IGameRuleContainer,
                                                             IGameRuleContainer,
                                                             RuleCollectionResponse,
                                                             RuleResponse> {
    
    protected static final MockRuleManager mockRuleManager = MockRuleManager.createInstance();
    
    public RuleControllerTest() {
        super("RuleControllerTest", 
              IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_PATH,
              mockRuleManager,
              mockRuleManager.getComparator());
    }
    
    
    //CREATE
    @Test
    public void testCreateElement() throws Exception {
        IGameRuleContainer container = mockRuleManager.createContainer(mockRuleManager.createElement(null));
        super.testCreateElement("testCreateRule", container, 
                                makeBaseRelativeUrlExpanded(container));
    }
    
    @Override
    protected Rule manageElementToCreate(Rule element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        IGameRuleContainer container = mockRuleManager.createContainer(mockRuleManager.createUsagePointsBasicActionPointsRule());
        super.testReadCollection("testReadRules", 
                                 container, 
                                 RuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
        
        container = mockRuleManager.createContainer(mockRuleManager.createGreenLeavesParamActionPointsRule());
        super.testReadCollection("testReadRules", 
                                 container, 
                                 RuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
        
        container = mockRuleManager.createContainer(mockRuleManager.createEcologicalBadgesFirstTimeActionBadgesRule());
        super.testReadCollection("testReadRules", 
                                 container, 
                                 RuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
        
        container = mockRuleManager.createContainer(mockRuleManager.createEcologicalBadgesParamPointTotalBadgesRule());
        super.testReadCollection("testReadRules", 
                                 container, 
                                 RuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
    }
    
    @Override
    protected List<Rule> retrieveCollection(RuleCollectionResponse response) {
        return (List<Rule>) response.getRules();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        IGameRuleContainer container = mockRuleManager.createContainer(mockRuleManager.createElement(null));
        super.testReadElementById("testReadRuleById", 
                                  container, 
                                  RuleResponse.class,
                                  makeBaseRelativeUrlExpanded(container));
    }

    @Override
    protected Rule manageNegativeElementToReadById(Rule element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected Rule retrieveSingleElement(RuleResponse response) {
        return response.getRule();
    }
    
    
    //UPDATE
    @Test
    public void testUpdateElement() throws Exception {
        IGameRuleContainer container = mockRuleManager.createContainer(mockRuleManager.createElement(null));
        super.testUpdateElement("testUpdateRule", 
                                container,
                                makeBaseRelativeUrlExpanded(container));
    }

    @Override
    protected Rule managePositiveElementToUpdate(Rule element) {
        element.setLhsOperand("2");
        
        return element;
    }

    @Override
    protected Rule manageNegativeElementToUpdate(Rule element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        IGameRuleContainer container = mockRuleManager.createContainer(mockRuleManager.createElement(null));
        super.testDeleteElement("testDeleteRule", 
                                container,
                                makeBaseRelativeUrlExpanded(container));
    }
    
    @Override
    protected Rule manageNegativeElementToDelete(Rule element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(Rule element) {
        return element.getId().toString();
    }

    
    protected Rule setNegativeId(Rule element) {
        element.setId(-1);
        
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(IGameRuleContainer container) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, container.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, container.getRule().getRuleTemplate().getPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, container.getRule().getCustomizedPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM, container.getRule().getRuleTemplate().getId());
        
        return super.expandUrl(this.baseRelativeUrl, uriVariables);
    }
}