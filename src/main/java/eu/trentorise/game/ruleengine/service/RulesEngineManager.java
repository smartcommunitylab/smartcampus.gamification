package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.model.backpack.Badge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.model.player.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        rulesPreparerManager.prepareRules(knowledgeBuilder, 
                                          gamificationApproachId);
        
        //TODO: refactoring anche dell'inizializzazione dei fatti
        Collection facts = initFacts();
        rulesExecutionManager.executeRules(knowledgeBuilder, facts);
        
        //TODO: refactoring del logResults
        logResults(facts);
        
        //TODO: implementare anche questa parte
        /*facts = initFacts();
        
        addNewRule(kbuilder, kbase);
        fireRules(ksession, collection);
        
        logResults(collection);*/
    }
 
    protected Collection initFacts() {
        List elements = new ArrayList();
        
        Badge badge = new Badge("Basic Mayor", new Integer(10));
        
        elements.add(badge);
        
        badge = new Badge("Enhanced Mayor", new Integer(100));
        
        elements.add(badge);
        
        badge = new Badge("Advanced Mayor", new Integer(1000));
        
        elements.add(badge);
        
        Player player = new Player("firstPlayer");
        
        elements.add(player);
        
        player = new Player("secondPlayer");
        player.setPoints(new Integer(10));
        
        elements.add(player);
        
        player = new Player("thirdPlayer");
        player.setPoints(new Integer(100));
        
        elements.add(player);
        
        player = new Player("fourthPlayer");
        player.setPoints(new Integer(1000));
        
        elements.add(player);
        
        //ksession.insert(messages);
        
        return elements;
    }

    protected void logResults(Collection collection) {
        logger.info("******************* LOG RESULTS *******************");
        
        for (Object object : collection) {
            logger.info(object.toString());
        }
    }
    
    
    @Qualifier("droolsKnowledgeBuilder")
    @Autowired
    protected IKnowledgeBuilder knowledgeBuilder;
    
    
    @Qualifier("rulesPreparerManager")
    @Autowired
    protected IRulesPreparerManager rulesPreparerManager;
    @Qualifier("droolsRulesExecutionManager")
    @Autowired
    protected IRulesExecutionManager rulesExecutionManager;
}