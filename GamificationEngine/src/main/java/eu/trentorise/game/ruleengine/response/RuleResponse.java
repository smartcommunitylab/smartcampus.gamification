package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.ruleengine.model.Rule;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class RuleResponse extends GameResponse {

    protected List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "RuleResponse{" + "rules=" + rules + '}';
    }
}