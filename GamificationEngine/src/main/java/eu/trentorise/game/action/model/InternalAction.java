package eu.trentorise.game.action.model;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.ruleengine.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public class InternalAction extends Action {
    
    protected CustomizedPlugin customizedPlugin;
    
    protected RuleTemplate ruleTemplate;

    public CustomizedPlugin getCustomizedPlugin() {
        return customizedPlugin;
    }

    public void setCustomizedPlugin(CustomizedPlugin customizedPlugin) {
        this.customizedPlugin = customizedPlugin;
    }

    public RuleTemplate getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplate ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    @Override
    public String toString() {
        return "InternalAction{" + "customizedPlugin=" + customizedPlugin + ", ruleTemplate=" + ruleTemplate + '}';
    }
}