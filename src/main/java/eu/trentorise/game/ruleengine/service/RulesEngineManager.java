package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.ruleengine.service.preparer.IRulesPreparerManager;
import eu.trentorise.game.ruleengine.service.executor.IRulesExecutionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.ruleengine.data.IFactsDAO;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * 
 * @author Luca Piras
 */
public class RulesEngineManager implements IRulesEngineManager {

    private static Logger logger = LoggerFactory.getLogger(RulesEngineManager.class.getName());
    
    protected IKnowledgeBuilder knowledgeBuilder;
    
    protected IFactsDAO factsDAO;
    
    protected IRulesPreparerManager rulesPreparerManager;
    protected IRulesPreparerManager addNewRulePreparerManager;
    
    
    @Override
    public void runEngine(Collection facts, GamificationPluginIdentifier gamificationApproachId) {
        //first rules execution
        this.runRules(facts, rulesPreparerManager, gamificationApproachId);
        
        //TODO: this one probably will be cleaned
        if (null != addNewRulePreparerManager) {
            //second rules execution
            this.runRules(facts, addNewRulePreparerManager, gamificationApproachId);
        }
    }
    
    protected void runRules(Collection facts, IRulesPreparerManager rpm, 
                            GamificationPluginIdentifier gamificationApproachId) {
        
        rpm.prepareRules(knowledgeBuilder, gamificationApproachId);
        
        Collection actualFacts = facts;
        if (null == actualFacts) {
            actualFacts = this.factsDAO.getFacts();
        }
        
        rulesExecutionManager.executeRules(knowledgeBuilder, actualFacts);
        
        logResults(actualFacts);
    }

    protected void logResults(Collection collection) {
        if (logger.isDebugEnabled()) {
            logger.debug("******************* LOG RESULTS *******************");

            for (Object object : collection) {
                logger.debug(object.toString());
            }
        }
    }

    
    public IKnowledgeBuilder getKnowledgeBuilder() {
        return knowledgeBuilder;
    }

    public void setKnowledgeBuilder(IKnowledgeBuilder knowledgeBuilder) {
        this.knowledgeBuilder = knowledgeBuilder;
    }

    public IFactsDAO getFactsDAO() {
        return factsDAO;
    }

    public void setFactsDAO(IFactsDAO factsDAO) {
        this.factsDAO = factsDAO;
    }

    public IRulesPreparerManager getRulesPreparerManager() {
        return rulesPreparerManager;
    }

    public void setRulesPreparerManager(IRulesPreparerManager rulesPreparerManager) {
        this.rulesPreparerManager = rulesPreparerManager;
    }

    public IRulesPreparerManager getAddNewRulePreparerManager() {
        return addNewRulePreparerManager;
    }

    public void setAddNewRulePreparerManager(IRulesPreparerManager addNewRulePreparerManager) {
        this.addNewRulePreparerManager = addNewRulePreparerManager;
    }
    
    
    @Qualifier("droolsRulesExecutionManager")
    @Autowired
    protected IRulesExecutionManager rulesExecutionManager;
}