package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.plugin.PluginIdentifier;
import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IRuleTemplateContainer;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public interface IRuleEngineManager {
    
    public void runEngine(Collection facts, PluginIdentifier gamificationApproachId) throws Exception;
}