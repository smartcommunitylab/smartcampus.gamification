package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.ruleengine.model.RuleTemplate;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplateCollectionResponse {
    
    protected Collection<RuleTemplate> ruleTemplates;

    public Collection<RuleTemplate> getRuleTemplates() {
        return ruleTemplates;
    }

    public void setRuleTemplates(Collection<RuleTemplate> ruleTemplates) {
        this.ruleTemplates = ruleTemplates;
    }

    @Override
    public String toString() {
        return "RuleTemplateCollectionResponse{" + "ruleTemplates=" + ruleTemplates + '}';
    }
}