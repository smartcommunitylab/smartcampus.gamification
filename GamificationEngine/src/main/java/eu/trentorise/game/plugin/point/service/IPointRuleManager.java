package eu.trentorise.game.plugin.point.service;

import eu.trentorise.game.plugin.point.model.rule.PointTemplateRule;
import eu.trentorise.game.response.GameResponse;


/**
 *
 * @author Luca Piras
 */
public interface IPointRuleManager {
    
    public GameResponse add(PointTemplateRule rule);
}