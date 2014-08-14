package eu.trentorise.game.ruleengine.comparator;

import eu.trentorise.game.ruleengine.model.Rule;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("ruleKeyComparator")
public class RuleKeyComparator implements Comparator<Rule>{

    @Override
    public int compare(Rule o1, Rule o2) {
        int finalComparison = o1.getId().compareTo(o2.getId());
        if (0 == finalComparison) {
            finalComparison = ruleTemplateKeyComparator.compare(o1.getRuleTemplate(), o2.getRuleTemplate());
        }
        
        return finalComparison;
    }

    
    public void setRuleTemplateKeyComparator(Comparator<RuleTemplate> ruleTemplateKeyComparator) {
        this.ruleTemplateKeyComparator = ruleTemplateKeyComparator;
    }
    
    
    @Qualifier("ruleTemplateKeyComparator")
    @Autowired
    protected Comparator<RuleTemplate> ruleTemplateKeyComparator;
}