package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.ruleengine.container.IRuleTemplateContainer;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;

/**
 *
 * @author Luca Piras
 */
public interface IRuleTemplateManager {

    public RuleTemplateResponse getRuleTemplateList(IRuleTemplateContainer container) throws Exception;
}