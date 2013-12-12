package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.ruleengine.service.preparer.IRulesPreparerManager;
import eu.trentorise.game.ruleengine.service.executor.IRulesExecutionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.ruleengine.data.IFactsDAO;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * 
 * @author Luca Piras
 */
@Service("rulesEngineManager")
public class RulesEngineManager implements IRulesEngineManager {

    private static Logger logger = LoggerFactory.getLogger(RulesEngineManager.class.getName());
    
    @Override
    public void runEngine(Integer gamificationApproachId) {
        //first rules execution
        this.runRules(rulesPreparerManager, gamificationApproachId);
        
        //second rules execution
        this.runRules(addNewRulePreparerManager, gamificationApproachId);
    }
    
    public void runRules(IRulesPreparerManager rpm, 
                         Integer gamificationApproachId) {
        
        rpm.prepareRules(knowledgeBuilder, gamificationApproachId);
        
        Collection facts = factsDAO.getFacts();
        
        rulesExecutionManager.executeRules(knowledgeBuilder, facts);
        
        logResults(facts);
    }

    protected void logResults(Collection collection) {
        if (logger.isDebugEnabled()) {
            logger.info("******************* LOG RESULTS *******************");

            for (Object object : collection) {
                logger.debug(object.toString());
            }
        }
    }
    
    
    @Qualifier("droolsKnowledgeBuilder")
    @Autowired
    protected IKnowledgeBuilder knowledgeBuilder;
        
    @Qualifier("mockFactsDAO")
    @Autowired
    protected IFactsDAO factsDAO;    
    
    @Qualifier("rulesPreparerManager")
    @Autowired
    protected IRulesPreparerManager rulesPreparerManager;
    @Qualifier("mockAddNewRulePreparerManager")
    @Autowired
    protected IRulesPreparerManager addNewRulePreparerManager;
    @Qualifier("droolsRulesExecutionManager")
    @Autowired
    protected IRulesExecutionManager rulesExecutionManager;
}