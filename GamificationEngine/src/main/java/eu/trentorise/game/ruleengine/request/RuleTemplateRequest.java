package eu.trentorise.game.ruleengine.request;

import eu.trentorise.game.ruleengine.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplateRequest {

    protected RuleTemplate ruleTemplate;

    public RuleTemplate getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplate ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    @Override
    public String toString() {
        return "RuleTemplateRequest{" + "ruleTemplate=" + ruleTemplate + '}';
    }
}