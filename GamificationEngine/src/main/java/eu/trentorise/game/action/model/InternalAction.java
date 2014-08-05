package eu.trentorise.game.action.model;

import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.ruleengine.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public class InternalAction extends Action {
    
    protected Plugin plugin;
    
    protected RuleTemplate ruleTemplate;

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public RuleTemplate getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplate ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    @Override
    public String toString() {
        return "InternalAction{" + "plugin=" + plugin + ", ruleTemplate=" + ruleTemplate + " " + super.toString() + " " + '}';
    }
}