package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.ruleengine.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public interface IRuleTemplateContainer {

    public RuleTemplate getRuleTemplate();
    public void setRuleTemplate(RuleTemplate ruleTemplate);
}