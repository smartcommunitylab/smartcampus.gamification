package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.service.MockActionManager;
import eu.trentorise.game.action.service.MockExternalActionParamManager;
import eu.trentorise.game.action.service.MockGameInternalActionManager;
import eu.trentorise.game.action.service.MockInternalActionParamManager;
import eu.trentorise.game.plugin.badgecollection.service.MockBadgeManager;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Reward;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.ruleengine.comparator.RuleKeyComparator;
import eu.trentorise.game.ruleengine.container.GameRuleContainer;
import eu.trentorise.game.ruleengine.container.IGameRuleContainer;
import eu.trentorise.game.ruleengine.model.Operator;
import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockRuleManager")
public class MockRuleManager implements IRestCrudManager<Rule, IGameRuleContainer, IGameRuleContainer>,
                                        IRestCrudTestManager<Rule, IGameRuleContainer, IGameRuleContainer> {
    
    
    public static MockRuleManager createInstance() {
        MockRuleManager mock = new MockRuleManager();
        mock.mockRuleTemplateManager = MockRuleTemplateManager.createInstance();
        mock.mockActionManager = MockActionManager.createInstance();
        mock.mockExternalActionParamManager = MockExternalActionParamManager.createInstance();
        mock.mockGameInternalActionManager = MockGameInternalActionManager.createInstance();
        mock.mockInternalActionParamManager = MockInternalActionParamManager.createInstance();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockBadgeManager = MockBadgeManager.createInstance();
        mock.mockGameProfileManager = MockGameProfileManager.createInstance();
        
        mock.ruleTemplateKeyComparator = mock.mockRuleTemplateManager.getComparator();
        mock.comparator = new RuleKeyComparator();
        ((RuleKeyComparator) mock.comparator).setRuleTemplateKeyComparator(mock.mockRuleTemplateManager.getComparator());
        
        return mock;
    }
    
    @Override
    public Rule createSingleElement(IGameRuleContainer containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return this.createElement(containerWithForeignIds);
    }

    @Override
    public Collection<Rule> readCollection(IGameRuleContainer containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella Rule e recupera tutti i
        //customizedPlugins per il plugin indicato
        return this.createElements(containerWithIds);
    }

    @Override
    public Rule readSingleElement(IGameRuleContainer containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        Rule returnValue = null;
        
        Rule expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds.getRule(), expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public Rule updateSingleElement(IGameRuleContainer containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        Rule returnValue = null;
        
        Rule expectedElement = this.createElement(containerWithForeignIds);
        if (0 == comparator.compare(containerWithForeignIds.getRule(), expectedElement)) {
            returnValue = containerWithForeignIds.getRule();
        }
        
        return returnValue;
    }

    @Override
    public Rule deleteSingleElement(IGameRuleContainer containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        Rule returnValue = null;
        
        Rule expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds.getRule(), expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    @Override
    public Rule createElement(IGameRuleContainer containerWithIds) throws Exception {
        return this.createGreenLeavesParamActionPointsRule();
    }
    
    /*@Override
    public Rule createElement(IGameRuleContainer containerWithIds) throws Exception {
        Rule element = null;
        
        Rule containerRule = containerWithIds.getRule();
        List<IGameRuleContainer> singleElements = this.createSingleElements();
        for (int i = 0; null == element && i < singleElements.size(); i++) {
            Rule current = singleElements.get(i).getRule();
            //CREATE
            if (null == containerRule.getId()) {
                if (0 == ruleTemplateKeyComparator.compare(containerRule.getRuleTemplate(), current.getRuleTemplate())) {
                    element = current;
                }
            } else if (0 == comparator.compare(containerRule, current)) {
                element = current;
            }
        }
        
        return element;
    }*/
    
    @Override
    public Collection createElements(IGameRuleContainer containerWithIds) throws Exception {
        Collection<Rule> elements = new ArrayList<>();
        
        RuleTemplate ruleTemplate = containerWithIds.getRule().getRuleTemplate();
        
        if (0 == ruleTemplateKeyComparator.compare(ruleTemplate, mockRuleTemplateManager.createBasicActionPointsRuleTemplate())) {
            elements = this.createBasicActionPointsRulesCollection();
        } else if (0 == ruleTemplateKeyComparator.compare(ruleTemplate, mockRuleTemplateManager.createParamActionPointsRuleTemplate())) {
            elements = this.createParamActionPointsRulesCollection();
        } else if (0 == ruleTemplateKeyComparator.compare(ruleTemplate, mockRuleTemplateManager.createFirstTimeActionBadgesRuleTemplate())) {
            elements = this.createFirstTimeActionBadgesRulesCollection();
        } else if (0 == ruleTemplateKeyComparator.compare(ruleTemplate, mockRuleTemplateManager.createParamPointTotalBadgesRuleTemplate())) {
            elements = this.createParamPointTotalBadgesRulesCollection();
        }
        
        return elements;
    }
    
    /*public List<IGameRuleContainer> createSingleElements() throws Exception {
        List<IGameRuleContainer> elements = new ArrayList<>();
        
        elements.add(this.createContainer(this.createUsagePointsBasicActionPointsRule()));
        elements.add(this.createContainer(this.createGreenLeavesParamActionPointsRule()));
        elements.add(this.createContainer(this.createEcologicalBadgesFirstTimeActionBadgesRule()));
        elements.add(this.createContainer(this.createEcologicalBadgesParamPointTotalBadgesRule()));
        
        return elements;
    }*/
    
    public IGameRuleContainer createContainer(Rule rule) {
        IGameRuleContainer container = new GameRuleContainer();
        
        container.setGame(mockGameProfileManager.createElement());
        container.setRule(rule);
        
        return container;
    }
    
    public List<Rule> createBasicActionPointsRulesCollection() throws Exception {
        List<Rule> list = new ArrayList<>();
        
        list.add(this.createUsagePointsBasicActionPointsRule());
        
        return list;
    }
    
    public List<Rule> createParamActionPointsRulesCollection() throws Exception {
        List<Rule> list = new ArrayList<>();
        
        list.add(this.createGreenLeavesParamActionPointsRule());
        
        return list;
    }
    
    public List<Rule> createFirstTimeActionBadgesRulesCollection() throws Exception {
        List<Rule> list = new ArrayList<>();
        
        list.add(this.createEcologicalBadgesFirstTimeActionBadgesRule());
        
        return list;
    }
    
    public List<Rule> createParamPointTotalBadgesRulesCollection() throws Exception {
        List<Rule> list = new ArrayList<>();
        
        list.add(this.createEcologicalBadgesParamPointTotalBadgesRule());
        
        return list;
    }
    
    protected Rule createRule(Integer ruleId,
                              RuleTemplate ruleTemplate,
                              Action action,
                              BasicParam lhsBasicParam,
                              Operator lhsOperator,
                              String lhsOperand,
                              CustomizedPlugin customizedPlugin,
                              Operator rhsPluginOperator,
                              String rhsValue,
                              BasicParam rhsBasicParam,
                              Operator rhsOperator,
                              String rhsOperand,
                              Reward reward,
                              String content) {
        
        Rule element = new Rule();
        element.setId(ruleId);
        element.setRuleTemplate(ruleTemplate);
        
        //trigger
        element.setAction(action);
    
        //leftHandSide
        element.setLhsBasicParam(lhsBasicParam);
        element.setLhsOperator(lhsOperator);
        element.setLhsOperand(lhsOperand);

        //customizedPluginInvolved
        element.setCustomizedPlugin(customizedPlugin);

        //rightHandSide
        element.setRhsPluginOperator(rhsPluginOperator);
        element.setRhsValue(rhsValue);
        element.setRhsBasicParam(rhsBasicParam);
        element.setRhsOperator(rhsOperator);
        element.setRhsOperand(rhsOperand);
        element.setReward(reward);

        element.setContent(content);
        
        return element;
    }

    public Rule createUsagePointsBasicActionPointsRule() throws Exception {
        Rule rule = this.createRule(//ruleId
                                    0,
                                    //ruleTemplate
                                    mockRuleTemplateManager.createBasicActionPointsRuleTemplate(),
                                    //externalAction
                                    mockActionManager.createBusDelayReportingAction(), 
                                    //leftCondition
                                    null, 
                                    null, 
                                    null,
                                    //customizedPlugin and related operator
                                    mockPluginManager.createUsagePointsPointPlugin(),
                                    mockRuleTemplateManager.createOperator("+="),
                                    //value
                                    "100",
                                    //rightCondition
                                    mockExternalActionParamManager.createBikeKmParam(),
                                    mockRuleTemplateManager.createOperator("*"),
                                    "10",
                                    //reward
                                    null,
                                    //content
                                    null);
        
        return rule;
    }
    
    public Rule createGreenLeavesParamActionPointsRule() throws Exception {        
        Rule rule = this.createRule(//ruleId
                                    0,
                                    //ruleTemplate
                                    mockRuleTemplateManager.createParamActionPointsRuleTemplate(),
                                    //externalAction
                                    mockActionManager.createItineratySavingAction(),
                                    //leftCondition
                                    mockExternalActionParamManager.createBikeKmParam(), 
                                    mockRuleTemplateManager.createOperator(">"), 
                                    "4",
                                    //customizedPlugin and related operator
                                    mockPluginManager.createGreenLeavesPointPlugin(),
                                    mockRuleTemplateManager.createOperator("+="),
                                    //value
                                    null,
                                    //rightCondition
                                    mockExternalActionParamManager.createBikeKmParam(),
                                    mockRuleTemplateManager.createOperator("*"),
                                    "10",
                                    //reward
                                    null,
                                    //content
                                    null);
        
        return rule;
    }
    
    public Rule createEcologicalBadgesFirstTimeActionBadgesRule() throws Exception {
        Rule rule = this.createRule(//ruleId
                                    0,
                                    //ruleTemplate
                                    mockRuleTemplateManager.createFirstTimeActionBadgesRuleTemplate(),
                                    //externalAction
                                    mockActionManager.createItineratySavingAction(),
                                    //leftCondition 
                                    null, 
                                    null, 
                                    null,
                                    //customizedPlugin and related operator
                                    mockPluginManager.createGreenLeavesPointPlugin(),
                                    null,
                                    //value
                                    null,
                                    //rightCondition
                                    null,
                                    null,
                                    null,
                                    //reward
                                    mockBadgeManager.createGreenHeroOne(),
                                    //content
                                    null);
        
        return rule;
    }
    
    public Rule createEcologicalBadgesParamPointTotalBadgesRule() throws Exception {
        Rule rule = this.createRule(//ruleId
                                    0,
                                    //ruleTemplate
                                    mockRuleTemplateManager.createParamPointTotalBadgesRuleTemplate(),
                                    //internalAction
                                    mockGameInternalActionManager.createGameGreenLeavesUpdatingInternalAction().getInternalAction(),
                                    //leftCondition 
                                    mockInternalActionParamManager.createGreenLeavesPointsUpdatingParam(), 
                                    mockRuleTemplateManager.createOperator(">="), 
                                    "1000",
                                    //customizedPlugin and related operator
                                    mockPluginManager.createGreenLeavesPointPlugin(),
                                    null,
                                    //value
                                    null,
                                    //rightCondition
                                    null,
                                    null,
                                    null,
                                    //reward
                                    mockBadgeManager.createGreenHeroOne(),
                                    //content
                                    null);
        
        return rule;
    }
    
    
    public Comparator<Rule> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockRuleTemplateManager")
    @Autowired
    protected MockRuleTemplateManager mockRuleTemplateManager;
    
    @Qualifier("mockActionManager")
    @Autowired
    protected MockActionManager mockActionManager;
    
    @Qualifier("mockExternalActionParamManager")
    @Autowired
    protected MockExternalActionParamManager mockExternalActionParamManager;
    
    @Qualifier("mockGameInternalActionManager")
    @Autowired
    protected MockGameInternalActionManager mockGameInternalActionManager;
    
    @Qualifier("mockInternalActionParamManager")
    @Autowired
    protected MockInternalActionParamManager mockInternalActionParamManager;
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    @Qualifier("mockBadgeCollectionPluginBadgeManager")
    @Autowired
    protected MockBadgeManager mockBadgeManager;
    
    @Qualifier("mockGameProfileManager")
    @Autowired
    protected MockGameProfileManager mockGameProfileManager;
    
    
    @Qualifier("ruleTemplateKeyComparator")
    @Autowired
    protected Comparator<RuleTemplate> ruleTemplateKeyComparator;
    
    @Qualifier("ruleKeyComparator")
    @Autowired
    protected Comparator<Rule> comparator;
}