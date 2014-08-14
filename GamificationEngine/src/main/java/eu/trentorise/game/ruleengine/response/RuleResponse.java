package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.ruleengine.model.Rule;

/**
 *
 * @author Luca Piras
 */
public class RuleResponse {

    protected Rule rule;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "RuleResponse{" + "rule=" + rule + '}';
    }
}