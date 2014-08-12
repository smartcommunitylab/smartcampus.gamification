package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.ruleengine.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplateResponse {
    
    protected RuleTemplate ruleTemplate;

    public RuleTemplate getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplate ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    @Override
    public String toString() {
        return "RuleTemplateResponse{" + "ruleTemplate=" + ruleTemplate + '}';
    }
}