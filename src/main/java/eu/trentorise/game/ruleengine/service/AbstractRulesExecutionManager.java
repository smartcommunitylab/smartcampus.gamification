package eu.trentorise.game.ruleengine.service;

import java.util.Collection;
import org.slf4j.Logger;

/**
 * To create knowledgeBase is heavy, to create knowledgeSession is light
 * 
 * @author Luca Piras
 * @param <B> knowledgeBase
 * @param <S> knowledgeSession
 */
public abstract class AbstractRulesExecutionManager<B,S> implements IRulesExecutionManager {
    
    protected Logger logger;
    
    public AbstractRulesExecutionManager(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void executeRules(IKnowledgeBuilder knowledgeBuilder,
                             Collection facts) {
        
        //if (null == this.knowledgeBase) {
            //this.initKnowledgeBase();
        //}
        
        S session = this.prepareSession(knowledgeBuilder);
        
        this.fireRules(knowledgeBuilder, facts, session);
    }
    
    //protected abstract void initKnowledgeBase();
    
    protected S prepareSession(IKnowledgeBuilder knowledgeBuilder) {
        // Check the builder for errors
        if (knowledgeBuilder.hasErrors()) {
            logger.error(knowledgeBuilder.getErrors().toString());
            throw new RuntimeException("Unable to compile rules\"!!!");
        }
 
        return this.buildSession(knowledgeBuilder);
    }
    
    protected abstract S buildSession(IKnowledgeBuilder knowledgeBuilder);

    protected abstract void fireRules(IKnowledgeBuilder knowledgeBuilder,
                                      Collection facts, S session);
}