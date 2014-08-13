package eu.trentorise.game.action.comparator;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.InternalAction;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
//not used at the moment
//@Component("internalActionKeyFkComparator")
public class InternalActionKeyFkComparator implements Comparator<InternalAction>{

    @Override
    public int compare(InternalAction o1, InternalAction o2) {
        int finalComparison = actionKeyComparator.compare(o1, o2);
        if (0 == finalComparison) {
            finalComparison = customizedPluginKeyComparator.compare(o1.getCustomizedPlugin(), o2.getCustomizedPlugin());
            if (0 == finalComparison) {
                finalComparison = ruleTemplateKeyComparator.compare(o1.getRuleTemplate(), o2.getRuleTemplate());
            }
        }
        
        return finalComparison;
    }

    
    public void setActionKeyComparator(Comparator<Action> actionKeyComparator) {
        this.actionKeyComparator = actionKeyComparator;
    }
    
    public void setCustomizedPluginKeyComparator(Comparator<CustomizedPlugin> customizedPluginKeyComparator) {
        this.customizedPluginKeyComparator = customizedPluginKeyComparator;
    }

    public void setRuleTemplateKeyComparator(Comparator<RuleTemplate> ruleTemplateKeyComparator) {
        this.ruleTemplateKeyComparator = ruleTemplateKeyComparator;
    }
    
    
    @Qualifier("actionKeyComparator")
    @Autowired
    protected Comparator<Action> actionKeyComparator;
    
    @Qualifier("customizedPluginKeyComparator")
    @Autowired
    protected Comparator<CustomizedPlugin> customizedPluginKeyComparator;
    
    @Qualifier("ruleTemplateKeyComparator")
    @Autowired
    protected Comparator<RuleTemplate> ruleTemplateKeyComparator;
}