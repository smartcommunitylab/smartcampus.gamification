package eu.trentorise.game.application.model;

import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.rule.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public class InternalAction extends Action {
    
    protected GamificationPlugin plugin;
    
    protected RuleTemplate ruleTemplate;

    public GamificationPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(GamificationPlugin plugin) {
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