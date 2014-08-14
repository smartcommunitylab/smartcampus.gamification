package eu.trentorise.game.ruleengine.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.ruleengine.model.GameRule;
import eu.trentorise.game.ruleengine.response.GameRuleCollectionResponse;
import eu.trentorise.game.ruleengine.response.GameRuleResponse;
import eu.trentorise.game.ruleengine.service.MockGameRuleManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class GameRuleControllerTest extends AbstractRestCrudTest<GameRule, 
                                                                 GameRule,
                                                                 GameRule,
                                                                 GameRuleCollectionResponse,
                                                                 GameRuleResponse> {
    
    protected static final MockGameRuleManager mockRuleManager = MockGameRuleManager.createInstance();
    
    public GameRuleControllerTest() {
        super("RuleControllerTest", 
              IGameConstants.SERVICE_RULEENGINE_G_P_CP_RT_RULES_PATH,
              mockRuleManager,
              mockRuleManager.getComparator());
    }
    
    
    //CREATE
    @Test
    public void testCreateElement() throws Exception {
        GameRule element = mockRuleManager.createElement(null);
        super.testCreateElement("testCreateGameRule", element, 
                                makeBaseRelativeUrlExpanded(element));
    }
    
    @Override
    protected GameRule manageElementToCreate(GameRule element) {
        element.getRule().setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        GameRule container = mockRuleManager.createGameRule(mockRuleManager.createUsagePointsBasicActionPointsRule());
        super.testReadCollection("testReadGameRules", 
                                 container, 
                                 GameRuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
        
        container = mockRuleManager.createGameRule(mockRuleManager.createGreenLeavesParamActionPointsRule());
        super.testReadCollection("testReadGameRules", 
                                 container, 
                                 GameRuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
        
        container = mockRuleManager.createGameRule(mockRuleManager.createEcologicalBadgesFirstTimeActionBadgesRule());
        super.testReadCollection("testReadGameRules", 
                                 container, 
                                 GameRuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
        
        container = mockRuleManager.createGameRule(mockRuleManager.createEcologicalBadgesParamPointTotalBadgesRule());
        super.testReadCollection("testReadGameRules", 
                                 container, 
                                 GameRuleCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(container));
    }
    
    @Override
    protected List<GameRule> retrieveCollection(GameRuleCollectionResponse response) {
        return (List<GameRule>) response.getGameRules();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        GameRule element = mockRuleManager.createElement(null);
        super.testReadElementById("testReadGameRuleById", 
                                  element, 
                                  GameRuleResponse.class,
                                  makeBaseRelativeUrlExpanded(element));
    }

    @Override
    protected GameRule manageNegativeElementToReadById(GameRule element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected GameRule retrieveSingleElement(GameRuleResponse response) {
        return response.getGameRule();
    }
    
    
    //UPDATE
    @Test
    public void testUpdateElement() throws Exception {
        GameRule element = mockRuleManager.createElement(null);
        super.testUpdateElement("testUpdateGameRule", 
                                element,
                                makeBaseRelativeUrlExpanded(element));
    }

    @Override
    protected GameRule managePositiveElementToUpdate(GameRule element) {
        element.setActivated(false);
        
        return element;
    }

    @Override
    protected GameRule manageNegativeElementToUpdate(GameRule element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        GameRule element = mockRuleManager.createElement(null);
        super.testDeleteElement("testDeleteGameRule", 
                                element,
                                makeBaseRelativeUrlExpanded(element));
    }
    
    @Override
    protected GameRule manageNegativeElementToDelete(GameRule element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(GameRule element) {
        return element.getRule().getId().toString();
    }

    
    protected GameRule setNegativeId(GameRule element) {
        element.getRule().setId(-1);
        
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(GameRule gameRule) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, gameRule.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_SINGLE_PATH_PARAM, gameRule.getRule().getRuleTemplate().getPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM, gameRule.getRule().getCustomizedPlugin().getId());
        uriVariables.put(IGameConstants.SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM, gameRule.getRule().getRuleTemplate().getId());
        
        return super.expandUrl(this.baseRelativeUrl, uriVariables);
    }
}