package eu.trentorise.game.ruleengine.service;


/**
 *
 * @author Luca Piras
 */
public interface IRulesPreparerManager {
    
    public void prepareRules(IKnowledgeBuilder kbuilder, 
                             Integer gamificationApproachId);
}