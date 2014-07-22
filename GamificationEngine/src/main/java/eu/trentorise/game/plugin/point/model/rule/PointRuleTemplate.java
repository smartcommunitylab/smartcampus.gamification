package eu.trentorise.game.plugin.point.model.rule;

import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.rule.model.RuleTemplate;

/**
 *
 * @author Luca Piras
 */
public class PointRuleTemplate extends RuleTemplate {
    
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

    @Override
    public String toString() {
        return "PointTemplateRule{" + "action=" + action + ", rewardPoints=" + rewardPoints + '}';
    }
}