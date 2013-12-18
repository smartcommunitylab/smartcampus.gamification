package eu.trentorise.game.plugin.point.model.rule;

import eu.trentorise.game.rule.model.Action;
import eu.trentorise.game.rule.model.TemplateRule;

/**
 *
 * @author Luca Piras
 */
public class PointTemplateRule extends TemplateRule {
    
    protected Action action;
    
    protected Integer rewardPoints;

    
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Integer getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    
    
}