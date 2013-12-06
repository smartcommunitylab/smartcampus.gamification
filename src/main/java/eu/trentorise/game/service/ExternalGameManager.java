package eu.trentorise.game.service;

import java.util.Collection;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.agent.KnowledgeAgent;
import org.kie.internal.agent.KnowledgeAgentFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;

/**
 *
 * @author Luca Piras
 */
/**
 * Very useful to connect to Guvnor 5.5.0 final to retrieve a Knowledge Base
 * 
 * @author luca
 */
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