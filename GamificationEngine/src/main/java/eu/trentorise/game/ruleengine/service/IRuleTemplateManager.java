package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IPluginOperatorContainer;
import eu.trentorise.game.ruleengine.container.IRuleContainer;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleResponse;
import eu.trentorise.game.ruleengine.response.RuleSettingResponse;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public interface IRuleTemplateManager {
    
    public OperatorResponse getOperatorsSupported(IOperatorContainer container);

    public OperatorResponse getPluginOperatorsSupported(IPluginOperatorContainer container);

    public RuleSettingResponse setRule(IRuleContainer container);

    public RuleResponse getRules(IRuleContainer container);
}