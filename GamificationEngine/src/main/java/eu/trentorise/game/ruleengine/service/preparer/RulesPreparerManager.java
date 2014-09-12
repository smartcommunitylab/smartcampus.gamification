package eu.trentorise.game.ruleengine.service.preparer;


import eu.trentorise.game.plugin.PluginIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.data.IRulesDAO;
import eu.trentorise.game.ruleengine.service.IKnowledgeBuilder;
import java.util.ArrayList;
import java.util.List;


public class RulesPreparerManager implements IRulesPreparerManager {

    private static final Logger logger = LoggerFactory.getLogger(RulesPreparerManager.class.getName());
    
    @Override
    public void prepareRules(IKnowledgeBuilder kbuilder, 
                             PluginIdentifier gamificationApproachId) {
        
        List<Rule> rules = this.getRules(gamificationApproachId);
        this.addRules(kbuilder, rules);
    }
    
    protected List<Rule> getRules(PluginIdentifier gamificationApproachId) {
        List<Rule> rules = this.initRules();
        
        for (IRulesDAO dao : daos) {
            dao.getRules(rules);
        }
        
        return rules;
    }
    
    protected List<Rule> initRules() {
        //TODO: do we need to synchronize it?
        return new ArrayList<>();
    }
    
    protected void addRules(IKnowledgeBuilder kbuilder, List<Rule> rules) {
        for (Rule rule : rules) {
            kbuilder.addRule(rule);
        }
    }

    
    public List<IRulesDAO> getDaos() {
        return daos;
    }

    public void setDaos(List<IRulesDAO> daos) {
        this.daos = daos;
    }
    
    
    protected List<IRulesDAO> daos;
}