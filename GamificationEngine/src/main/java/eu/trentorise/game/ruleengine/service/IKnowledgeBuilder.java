package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.ruleengine.model.Rule;

/**
 *
 * @author Luca Piras
 */
public interface IKnowledgeBuilder {
    
    public void addRule(Rule rule);
    
    public boolean hasErrors();
    
    public String getErrors();
    
    public Object getKnowledgePackages();
}