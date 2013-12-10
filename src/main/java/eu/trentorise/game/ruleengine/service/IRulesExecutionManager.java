package eu.trentorise.game.ruleengine.service;

import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public interface IRulesExecutionManager {
    
    public void executeRules(IKnowledgeBuilder knowledgeBuilder,
                             Collection facts);
}