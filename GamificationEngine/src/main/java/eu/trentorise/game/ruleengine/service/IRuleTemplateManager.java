package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IPluginOperatorContainer;
import eu.trentorise.game.ruleengine.response.OperatorResponse;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public interface IRuleTemplateManager {
    
    public OperatorResponse getOperatorsSupported(IOperatorContainer container);

    public OperatorResponse getPluginOperatorsSupported(IPluginOperatorContainer container);
}