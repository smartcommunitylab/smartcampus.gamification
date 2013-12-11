package eu.trentorise.game.ruleengine.service.preparer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.rule.Rule;
import eu.trentorise.game.ruleengine.data.IRulesDAO;
import eu.trentorise.game.ruleengine.service.IKnowledgeBuilder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("rulesPreparerManager")
public class RulesPreparerManager implements IRulesPreparerManager {

    private static final Logger logger = LoggerFactory.getLogger(RulesPreparerManager.class.getName());
    
    @Override
    public void prepareRules(IKnowledgeBuilder kbuilder, 
                             Integer gamificationApproachId) {
        
        List<Rule> rules = this.getRules(gamificationApproachId);
        this.addRules(kbuilder, rules);
    }
    
    protected List<Rule> getRules(Integer gamificationApproachId) {
        return dao.getRules(gamificationApproachId);
    }
    
    protected void addRules(IKnowledgeBuilder kbuilder, List<Rule> rules) {
        for (Rule rule : rules) {
            kbuilder.addRule(rule);
        }
    }
    
    
    @Qualifier("templateRulesDAO")
    @Autowired
    protected IRulesDAO dao;
}