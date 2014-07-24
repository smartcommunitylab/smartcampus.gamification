package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.annotation.TransactionalGame;
import eu.trentorise.game.data.PlayerRepository;
import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.ruleengine.container.IOperatorContainer;
import eu.trentorise.game.ruleengine.container.IRuleTemplateContainer;
import eu.trentorise.game.ruleengine.service.preparer.IRulesPreparerManager;
import eu.trentorise.game.ruleengine.service.executor.IRulesExecutionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.ruleengine.data.IFactsDAO;
import eu.trentorise.game.ruleengine.response.OperatorResponse;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * 
 * @author Luca Piras
 */
public class RuleEngineManager implements IRuleEngineManager {

    private static Logger logger = LoggerFactory.getLogger(RuleEngineManager.class.getName());
    
    protected IKnowledgeBuilder knowledgeBuilder;
    
    protected IFactsDAO factsDAO;
    
    protected IRulesPreparerManager rulesPreparerManager;
    protected IRulesPreparerManager addNewRulePreparerManager;
    
    
    @Override
    public RuleTemplateResponse getRuleTemplates(IRuleTemplateContainer container) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OperatorResponse getOperatorsSupported(IOperatorContainer container) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //TODO: delete this method
    protected void dataJpaTry() throws Exception {
        Player player = new Player();
        player.setUsername("lucap");
        player.setPoints(0);
        
        repository.save(player);
        
        player.setUsername("OtherPlayer");
        
        repository.save(player);
        
        Player findOne = repository.findOne("lucap");
        findOne.setPoints(100);
        repository.save(findOne);
        
        // fetch all customers
        Iterable<Player> items = repository.findAll();
        logger.debug("Items found with findAll():");
        logger.debug("-------------------------------");
        for (Player item : items) {
            logger.debug(item + "");
        }
        logger.debug("/n");

        // fetch an individual customer by ID
        Player item = repository.findOne("OtherPlayer");
        logger.debug("Item found with findOne(OtherPlayer):");
        logger.debug("--------------------------------");
        logger.debug(item + "");
        logger.debug("/n");

        // fetch customers by last name
        List<Player> foundByPoints = repository.findByPoints(0);
        logger.debug("Player found with findByPoints(0):");
        logger.debug("--------------------------------------------");
        for (Player current : items) {
            logger.debug(current + "");
        }
        
        //throw new Exception();
    }
    
    protected void nonRepeatableReadAndPhantomRead() throws Exception {
        List<Player> items = repository.findByPoints(0);
        Player findOne = items.get(0);
        
        items = repository.findByPoints(0);
        findOne = items.get(0);
    }
    
    @TransactionalGame
    @Override
    public void runEngine(Collection facts, GamificationPluginIdentifier gamificationApproachId) throws Exception {
        //TODO: DELETE
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        //this.dataJpaTry();
        //this.nonRepeatableReadAndPhantomRead();
        //first rules execution
        this.runRules(facts, rulesPreparerManager, gamificationApproachId);
        
        //TODO: this one probably will be cleaned
        if (null != addNewRulePreparerManager) {
            //second rules execution
            this.runRules(facts, addNewRulePreparerManager, gamificationApproachId);
        }
        
        actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
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
    
    //TODO: DELETE
    @Autowired
    protected PlayerRepository repository;
}