package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.ruleengine.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplateContainer implements IRuleTemplateContainer {
    
    protected RuleTemplate ruleTemplate;

    @Override
    public RuleTemplate getRuleTemplate() {
        return ruleTemplate;
    }

    @Override
    public void setRuleTemplate(RuleTemplate ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    @Override
    public String toString() {
        return "RuleTemplateContainer{" + "ruleTemplate=" + ruleTemplate + '}';
    }
}