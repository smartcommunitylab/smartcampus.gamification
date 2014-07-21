package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.ruleengine.container.IRuleTemplateContainer;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.rule.model.RuleTemplate;
import eu.trentorise.game.rule.model.Type;
import java.util.ArrayList;
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
public class MockRuleTemplateManager extends MockResponder implements IRuleTemplateManager {

    @Override
    public RuleTemplateResponse getRuleTemplateList(IRuleTemplateContainer container) throws Exception {
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
    
    public List<RuleTemplate> createPointPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        GamificationPlugin plugin = manager.createPointsPlugin();
        
        list.add(this.createRuleTemplate(plugin, 0, "BasicActionPoints", Type.BASIC, "A user, by doing a specific action, can earn Usage Points"));
        list.add(this.createRuleTemplate(plugin, 1, "ParamActionPoints", Type.PARAMETRIC, "A user, by doing a specific action, can earn Usage Points"));
        
        return list;
    }
    
    public List<RuleTemplate> createBadgeCollectionPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        GamificationPlugin plugin = manager.createPointsPlugin();
        
        list.add(this.createRuleTemplate(plugin, 0, "FirstTimeActionBadges", Type.BASIC, "When an action happens for the first time, a user earn a badge"));
        list.add(this.createRuleTemplate(plugin, 1, "ParamFirstTimeActionBadges", Type.PARAMETRIC, "When an action happens for the first time, a user earn a badge"));
        list.add(this.createRuleTemplate(plugin, 2, "ParamPointTotalBadges", Type.PARAMETRIC, "Given a total of accumulated points a player can only earn once a badge"));
        
        return list;
    }
    
    public List<RuleTemplate> createLeaderboardPointPluginRuleTemplateList() throws Exception {
        List<RuleTemplate> list = new ArrayList<>();
        
        return list;
    }
    
    protected RuleTemplate createRuleTemplate(GamificationPlugin plugin, 
                                              Integer id, String name, 
                                              Type type, String description) {
        
        RuleTemplate ruleTemplate = new RuleTemplate();
        
        ruleTemplate.setPlugin(plugin);
        ruleTemplate.setId(id);
        ruleTemplate.setName(name);
        ruleTemplate.setType(type);
        ruleTemplate.setDescription(description);
        
        return ruleTemplate;
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