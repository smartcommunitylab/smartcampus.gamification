package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.data.CustomerRepository;
import eu.trentorise.game.model.player.Customer;
import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.ruleengine.service.preparer.IRulesPreparerManager;
import eu.trentorise.game.ruleengine.service.executor.IRulesExecutionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.ruleengine.data.IFactsDAO;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;


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
    
    
    //TODO: delete this method
    protected void dataJpaTry() throws Exception {
        /*List<Customer> customers = repository.findByLastName("Piras");
        Customer customer = customers.get(0);
        
        customer.setFirstName("Luca");
        repository.save(customer);
        
        throw new Exception();*/
        
        // save a couple of customers
        repository.save(new Customer("Jack", "Bauer"));
        repository.save(new Customer("Chloe", "O'Brian"));
        repository.save(new Customer("Kim", "Bauer"));
        repository.save(new Customer("David", "Palmer"));
        repository.save(new Customer("Michelle", "Dessler"));
        
        /*Customer findOne = repository.findOne(88L);
        findOne.setLastName("Piras10");
        repository.save(findOne);*/
        
        // fetch all customers
        Iterable<Customer> customers = repository.findAll();
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer by ID
        Customer customer = repository.findOne(1L);
        System.out.println("Customer found with findOne(1L):");
        System.out.println("--------------------------------");
        System.out.println(customer);
        System.out.println();

        // fetch customers by last name
        List<Customer> bauers = repository.findByLastName("Bauer");
        System.out.println("Customer found with findByLastName('Bauer'):");
        System.out.println("--------------------------------------------");
        for (Customer bauer : bauers) {
            System.out.println(bauer);
        }
        
        //throw new RuntimeException();
    }
    
    protected void nonRepeatableReadAndPhantomRead() throws Exception {
        List<Customer> customers = repository.findByLastName("Tower");
        Customer findOne = customers.get(0);
        
        customers = repository.findByLastName("Tower");
        findOne = customers.get(0);
    }
    
    @Transactional
    @Override
    public void runEngine(Collection facts, GamificationPluginIdentifier gamificationApproachId) throws Exception {
        //TODO: DELETE
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        //this.dataJpaTry();
        this.nonRepeatableReadAndPhantomRead();
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
    protected CustomerRepository repository;
}