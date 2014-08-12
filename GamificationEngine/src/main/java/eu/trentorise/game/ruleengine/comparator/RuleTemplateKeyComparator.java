package eu.trentorise.game.ruleengine.comparator;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("ruleTemplateKeyComparator")
public class RuleTemplateKeyComparator implements Comparator<RuleTemplate> {

    @Override
    public int compare(RuleTemplate o1, RuleTemplate o2) {
        int finalComparison = o1.getId().compareTo(o2.getId());
        if (0 == finalComparison) {
            finalComparison = pluginComparator.compare(o1.getPlugin(), o2.getPlugin());
        }
        
        return finalComparison;
    }

    
    public void setPluginComparator(Comparator<Plugin> pluginComparator) {
        this.pluginComparator = pluginComparator;
    }
    
    
    @Qualifier("pluginKeyComparator")
    @Autowired
    protected Comparator<Plugin> pluginComparator;
}