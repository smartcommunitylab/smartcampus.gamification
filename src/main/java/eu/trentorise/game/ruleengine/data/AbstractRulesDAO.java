package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.rule.Rule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractRulesDAO implements IRulesDAO {
    
    @Override
    public List<Rule> getRules(Integer gamificationApproachId) {
        
        List<String> contentRules = initContentRules();
        contentRules = obtainsContentRules(contentRules);
        
        List<Rule> rules = this.initRules();
        rules = buildRules(rules, contentRules);
        
        return rules;
    }

    protected List<String> initContentRules() {
        //TODO: do we need to synchronize it?
        return new ArrayList<>();
    }
    
    protected abstract List<String> obtainsContentRules(List<String> contentRules);
    
    protected List<Rule> initRules() {
        //TODO: do we need to synchronize it?
        return new ArrayList<>();
    }
    
    protected List<Rule> buildRules(List<Rule> rules, List<String> contentRules) {
        int id = 1;
        for (String element : contentRules) {
            Rule rule = new Rule("" + id, null, element);
            rules.add(rule);
            
            id++;
        }
        
        return rules;
    }
}