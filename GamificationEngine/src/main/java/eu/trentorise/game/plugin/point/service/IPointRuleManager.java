package eu.trentorise.game.plugin.point.service;

import eu.trentorise.game.plugin.point.model.rule.PointTemplateRule;
import eu.trentorise.game.response.SuccessResponse;


/**
 *
 * @author Luca Piras
 */
public interface IPointRuleManager {
    
    public SuccessResponse add(PointTemplateRule rule);
}