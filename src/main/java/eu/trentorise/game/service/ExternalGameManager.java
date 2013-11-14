package eu.trentorise.game.service;

import java.util.Collection;
import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service
public class ExternalGameManager extends GameManager {
    
    @Override
    protected Collection<KnowledgePackage> getKnowledgePackages(Object object) {
        KnowledgeBase kbase = this.readKnowledgeBase();
        
        return kbase.getKnowledgePackages();
    }
    
    protected KnowledgeBase readKnowledgeBase() {
        KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("kagent");      
        //ResourceFactory.getResourceChangeNotifierService().start();  
        //ResourceFactory.getResourceChangeScannerService().start();
        //kagent.setSystemEventListener(new PrintStreamSystemEventListener());  
        kagent.applyChangeSet(ResourceFactory.newClassPathResource("/conf/drools/change-set.xml"));
        KnowledgeBase kbase = kagent.getKnowledgeBase();
        
        return kbase;
    }
}