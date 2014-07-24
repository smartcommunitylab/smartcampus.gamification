package eu.trentorise.game.ruleengine.service.preparer;


import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.data.IRulesDAO;
import eu.trentorise.game.ruleengine.service.IKnowledgeBuilder;
import java.util.List;


public class RulesPreparerManager implements IRulesPreparerManager {

    private static final Logger logger = LoggerFactory.getLogger(RulesPreparerManager.class.getName());
    
    @Override
    public void prepareRules(IKnowledgeBuilder kbuilder, 
                             GamificationPluginIdentifier gamificationApproachId) {
        
        List<Rule> rules = this.getRules(gamificationApproachId);
        this.addRules(kbuilder, rules);
    }
    
    protected List<Rule> getRules(GamificationPluginIdentifier gamificationApproachId) {
        return dao.getRules(gamificationApproachId);
    }
    
    protected void addRules(IKnowledgeBuilder kbuilder, List<Rule> rules) {
        for (Rule rule : rules) {
            kbuilder.addRule(rule);
        }
    }

    public IRulesDAO getDao() {
        return dao;
    }

    public void setDao(IRulesDAO dao) {
        this.dao = dao;
    }
    
    
    protected IRulesDAO dao;
}