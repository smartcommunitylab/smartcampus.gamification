package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.ruleengine.model.Rule;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class RuleCollectionResponse {
    
    protected Collection<Rule> rules;

    public Collection<Rule> getRules() {
        return rules;
    }

    public void setRules(Collection<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "RuleCollectionResponse{" + "rules=" + rules + '}';
    }
}