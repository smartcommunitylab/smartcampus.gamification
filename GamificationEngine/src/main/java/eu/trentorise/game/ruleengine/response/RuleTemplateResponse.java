package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplateResponse extends GameResponse {
    
    protected List<RuleTemplate> ruleTemplates;

    public List<RuleTemplate> getRuleTemplates() {
        return ruleTemplates;
    }

    public void setRuleTemplates(List<RuleTemplate> ruleTemplates) {
        this.ruleTemplates = ruleTemplates;
    }

    @Override
    public String toString() {
        return "RuleTemplateResponse{" + "ruleTemplates=" + ruleTemplates + " " + super.toString() + " " + '}';
    }
}