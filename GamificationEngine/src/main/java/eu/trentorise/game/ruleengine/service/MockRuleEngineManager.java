package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.model.ParamType;
import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IRuleTemplateContainer;
import eu.trentorise.game.ruleengine.model.HandSideType;
import eu.trentorise.game.ruleengine.model.Operator;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.model.RuleTemplateType;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
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
@Service("mockRuleEngineManager")
public class MockRuleEngineManager extends MockResponder implements IRuleEngineManager {

    @Override
    public void runEngine(Collection facts, GamificationPluginIdentifier gamificationApproachId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public RuleTemplateResponse getRuleTemplates(IRuleTemplateContainer container) throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        GamificationPlugin plugin = container.getRuleTemplate().getPlugin();
        if (0 == comparator.compare(plugin, manager.createPointsPlugin())) {
            list = this.createPointPluginRuleTemplateList();
        } else if (0 == comparator.compare(plugin, manager.createBadgeCollectionPlugin())) {
            list = this.createBadgeCollectionPluginRuleTemplateList();
        } else if (0 == comparator.compare(plugin, manager.createLeadearboardPointPlugin())) {
            list = this.createLeaderboardPointPluginRuleTemplateList();
        }
        
        return this.makeCustomizedResponse(list);
    }
    
    @Override
    public OperatorResponse getOperatorsSupported(IOperatorContainer container) {
        return this.makeResponse(this.createElements(container.getParam(), 
                                                     container.getHandSideType()));
    }
    
    public List<RuleTemplate> createPointPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        GamificationPlugin plugin = manager.createPointsPlugin();
        
        list.add(this.createRuleTemplate(plugin, 0, "BasicActionPoints", RuleTemplateType.BASIC, "A user, by doing a specific action, can earn Usage Points"));
        list.add(this.createRuleTemplate(plugin, 1, "ParamActionPoints", RuleTemplateType.PARAMETRIC, "A user, by doing a specific action, can earn Usage Points"));
        
        return list;
    }
    
    public List<RuleTemplate> createBadgeCollectionPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        GamificationPlugin plugin = manager.createPointsPlugin();
        
        list.add(this.createRuleTemplate(plugin, 0, "FirstTimeActionBadges", RuleTemplateType.BASIC, "When an action happens for the first time, a user earn a badge"));
        list.add(this.createRuleTemplate(plugin, 1, "ParamFirstTimeActionBadges", RuleTemplateType.PARAMETRIC, "When an action happens for the first time, a user earn a badge"));
        list.add(this.createRuleTemplate(plugin, 2, "ParamPointTotalBadges", RuleTemplateType.PARAMETRIC, "Given a total of accumulated points a player can only earn once a badge"));
        
        return list;
    }
    
    public List<RuleTemplate> createLeaderboardPointPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        return list;
    }
    
    protected RuleTemplate createRuleTemplate(GamificationPlugin plugin, 
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
    
    public List<Operator> createElements(BasicParam param, 
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
    
    public List<Operator> createIntegerLeftHandSideOperators() {
        List<Operator> list = new ArrayList<>();
        
        list.add(this.createOperator(">"));
        list.add(this.createOperator(">="));
        list.add(this.createOperator("<"));
        list.add(this.createOperator("<="));
        list.add(this.createOperator("="));
        list.add(this.createOperator("!="));
        
        return list;
    }
    
    public List<Operator> createIntegerRightHandSideOperators() {
        List<Operator> list = new ArrayList<>();
        
        list.add(this.createOperator("*"));
        list.add(this.createOperator("/"));
        list.add(this.createOperator("+"));
        list.add(this.createOperator("-"));
        
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
    
    protected RuleTemplateResponse makeCustomizedResponse(List<RuleTemplate> list) {
        RuleTemplateResponse response = new RuleTemplateResponse();
        response.setRuleTemplates(list);
        
        return ((RuleTemplateResponse) this.buildPositiveResponse(response));
    }
    
    public void setManager(MockGamePluginManager manager) {
        this.manager = manager;
    }
    
    
    @Qualifier("mockGamePluginManager")
    @Autowired
    protected MockGamePluginManager manager;
    
    @Qualifier("gamificationPluginKeyComparator")
    @Autowired
    protected Comparator<GamificationPlugin> comparator;
}