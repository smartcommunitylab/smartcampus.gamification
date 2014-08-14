package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.model.ParamType;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.ruleengine.comparator.RuleTemplateKeyComparator;
import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IPluginOperatorContainer;
import eu.trentorise.game.ruleengine.model.HandSideType;
import eu.trentorise.game.ruleengine.model.Operator;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.model.RuleTemplateType;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockRuleTemplateManager")
public class MockRuleTemplateManager extends MockResponder implements IRuleTemplateManager,
                                                                      IRestCrudManager<RuleTemplate, Plugin, RuleTemplate>,
                                                                      IRestCrudTestManager<RuleTemplate, Plugin, RuleTemplate> {
    
    public static MockRuleTemplateManager createInstance() {
        MockRuleTemplateManager mock = new MockRuleTemplateManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        
        mock.pluginKeyComparator = mock.mockPluginManager.getComparator();
        
        mock.comparator = new RuleTemplateKeyComparator();
        ((RuleTemplateKeyComparator) mock.comparator).setPluginComparator(mock.pluginKeyComparator);
        
        return mock;
    }
    
    @Override
    public RuleTemplate createSingleElement(RuleTemplate containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<RuleTemplate> readCollection(Plugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella RuleTemplate e recupera tutti i
        //gli elementi relativi al plugin indicato
        return this.createElements(containerWithIds);
    }

    @Override
    public RuleTemplate readSingleElement(RuleTemplate containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        RuleTemplate returnValue = null;
        
        RuleTemplate expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public RuleTemplate updateSingleElement(RuleTemplate containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RuleTemplate deleteSingleElement(RuleTemplate containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public RuleTemplate createElement(RuleTemplate containerWithIds) throws Exception {
        return this.createBasicActionPointsRuleTemplate();
    }

    @Override
    public Collection<RuleTemplate> createElements(Plugin containerWithIds) throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        if (0 == pluginKeyComparator.compare(containerWithIds, mockPluginManager.createPointsPlugin())) {
            list = this.createPointPluginRuleTemplateList();
        } else if (0 == pluginKeyComparator.compare(containerWithIds, mockPluginManager.createBadgeCollectionPlugin())) {
            list = this.createBadgeCollectionPluginRuleTemplateList();
        } else if (0 == pluginKeyComparator.compare(containerWithIds, mockPluginManager.createLeadearboardPointPlugin())) {
            list = this.createLeaderboardPointPluginRuleTemplateList();
        }
        
        return list;
    }
    
    public List<RuleTemplate> createPointPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        list.add(this.createBasicActionPointsRuleTemplate());
        list.add(this.createParamActionPointsRuleTemplate());
        
        return list;
    }
    
    public List<RuleTemplate> createBadgeCollectionPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        Plugin plugin = mockPluginManager.createBadgeCollectionPlugin();
        
        list.add(this.createFirstTimeActionBadgesRuleTemplate());
        list.add(this.createRuleTemplate(plugin, 1, "ParamFirstTimeActionBadges", RuleTemplateType.PARAMETRIC, "When an action happens for the first time, a user earn a badge"));
        list.add(this.createParamPointTotalBadgesRuleTemplate());
        
        return list;
    }
    
    public List<RuleTemplate> createLeaderboardPointPluginRuleTemplateList() throws Exception {
        return new ArrayList<>();
    }
    
    protected RuleTemplate createRuleTemplate(Plugin plugin, 
                                              Integer id, String name, 
                                              RuleTemplateType type, String description) {
        
        RuleTemplate ruleTemplate = new RuleTemplate();
        
        ruleTemplate.setPlugin(plugin);
        ruleTemplate.setId(id);
        ruleTemplate.setName(name);
        ruleTemplate.setType(type);
        ruleTemplate.setDescription(description);
        
        return ruleTemplate;
    }
    
    protected RuleTemplate createBasicActionPointsRuleTemplate() {
        Plugin plugin = mockPluginManager.createPointsPlugin();
        return this.createRuleTemplate(plugin, 0, "BasicActionPoints", RuleTemplateType.BASIC, "A user, by doing a specific action, can earn Usage Points");
    }
    
    protected RuleTemplate createParamActionPointsRuleTemplate() {
        Plugin plugin = mockPluginManager.createPointsPlugin();
        return this.createRuleTemplate(plugin, 1, "ParamActionPoints", RuleTemplateType.PARAMETRIC, "A user, by doing a specific action, can earn Green Leaves");
    }
    
    protected RuleTemplate createFirstTimeActionBadgesRuleTemplate() {
        Plugin plugin = mockPluginManager.createBadgeCollectionPlugin();
        return this.createRuleTemplate(plugin, 0, "FirstTimeActionBadges", RuleTemplateType.BASIC, "When an action happens for the first time, a user earns a badge");
    }
    
    protected RuleTemplate createParamPointTotalBadgesRuleTemplate() {
        Plugin plugin = mockPluginManager.createBadgeCollectionPlugin();
        return this.createRuleTemplate(plugin, 2, "ParamPointTotalBadges", RuleTemplateType.PARAMETRIC, "Given a total of accumulated points a player can earn only once a badge");
    }
    
    
    @Override
    public OperatorResponse getOperatorsSupported(IOperatorContainer container) {
        return this.makeResponse(this.createOperators(container.getParam(), 
                                                     container.getHandSideType()));
    }
    
    @Override
    public OperatorResponse getPluginOperatorsSupported(IPluginOperatorContainer container) {
        return this.makeResponse(this.createPluginOperators(container.getGamificationPlugin()));
    }
    
        
    public List<Operator> createOperators(BasicParam param, 
                                          HandSideType handSideType) {
        
        List<Operator> list = new ArrayList<>();
        
        if (0 == param.getType().compareTo(ParamType.INTEGER)) {
            
            if (0 == handSideType.compareTo(HandSideType.LEFT)) {
                list = this.createIntegerLeftHandSideOperators();
            } else if (0 == handSideType.compareTo(HandSideType.RIGHT)) {
                list = this.createIntegerRightHandSideOperators();
            }
        }
        
        return list;
    }
    
    public List<Operator> createPluginOperators(Plugin plugin) {
        List<Operator> list = new ArrayList<>();
        
        if (null != plugin) {
            if (0 == pluginKeyComparator.compare(plugin, mockPluginManager.createPointsPlugin())) {
                list = this.createPointPluginOperators();
            }
        }
        
        return list;
    }
    
    public List<Operator> createIntegerLeftHandSideOperators() {
        List<Operator> list = new ArrayList<>();
        
        list.add(this.createOperator(">"));
        list.add(this.createOperator(">="));
        list.add(this.createOperator("<"));
        list.add(this.createOperator("<="));
        list.add(this.createOperator("=="));
        list.add(this.createOperator("!="));
        
        return list;
    }
    
    public List<Operator> createIntegerRightHandSideOperators() {
        List<Operator> list = new ArrayList<>();
        
        list.add(this.createOperator("+"));
        list.add(this.createOperator("-"));
        list.add(this.createOperator("*"));
        list.add(this.createOperator("/"));
        list.add(this.createOperator("^"));
        
        return list;
    }
    
    public List<Operator> createPointPluginOperators() {
        List<Operator> list = new ArrayList<>();
        
        list.add(this.createOperator("="));
        list.add(this.createOperator("+="));
        list.add(this.createOperator("-="));
        list.add(this.createOperator("*="));
        list.add(this.createOperator("/="));
        list.add(this.createOperator("^="));
        
        return list;
    }
    
    protected Operator createOperator(String symbol) {
        Operator element = new Operator();
        element.setSymbol(symbol);
        return element;
    }
    
    protected OperatorResponse makeResponse(List<Operator> list) {
        OperatorResponse response = new OperatorResponse();
        response.setOperators(list);
        
        return ((OperatorResponse) this.buildPositiveResponse(response));
    }
    
    
    public void setManager(MockPluginManager manager) {
        this.mockPluginManager = manager;
    }

    public Comparator<RuleTemplate> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    
    @Qualifier("pluginKeyComparator")
    @Autowired
    protected Comparator<Plugin> pluginKeyComparator;
    
    @Qualifier("ruleTemplateKeyComparator")
    @Autowired
    protected Comparator<RuleTemplate> comparator;
}