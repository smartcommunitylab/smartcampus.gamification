package eu.trentorise.game.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.model.Message;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatelessKnowledgeSession;


public class MessagesGameManager implements IGameManager {

    private static Logger logger = LoggerFactory.getLogger(MessagesGameManager.class.getName());
    
    @Override
    public void getGame() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        
        StatelessKnowledgeSession ksession = initDrools(kbuilder, kbase);
        Collection collection = initCollection();
        fireRules(ksession, collection);
        
        logResults(collection);
        
        resetResults(collection);
        
        addNewRule(kbuilder, kbase);
        addToCollection("Another", collection);
        fireRules(ksession, collection);
        
        logResults(collection);
    }
 
    protected StatelessKnowledgeSession initDrools(KnowledgeBuilder kbuilder, KnowledgeBase kbase) {
 
        // read rules from String
        String myRule = "import eu.trentorise.game.model.Message import java.util.ArrayList rule \"Hello World\" when $results : ArrayList( size >= 0 ) message:Message (type==\"Hello\") then $results.add(\"Hello World, Drools!\"); end";
        addRule(myRule, kbuilder);
        
        myRule = "import eu.trentorise.game.model.Message import java.util.ArrayList rule \"Hello World 2\" when $results : ArrayList( size >= 0 ) message:Message (type==\"Test\") then $results.add(\"Test, Drools!\"); end";
        addRule(myRule, kbuilder);
        
        return prepareNewSession(kbuilder, kbase);
    }
    
    protected void addRule(String rule, KnowledgeBuilder kbuilder) {
        Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(rule));
        kbuilder.add(myResource, ResourceType.DRL);
    }
 
    protected void addNewRule(KnowledgeBuilder kbuilder, KnowledgeBase kbase) {
        
        // read another rule from String
        String myRule = "import eu.trentorise.game.model.Message import java.util.ArrayList rule \"Hello World 3\" when $results : ArrayList( size >= 0 ) message:Message (type==\"Another\") then $results.add(\"Another one!\"); end";
        addRule(myRule, kbuilder);
        
        prepareNewSession(kbuilder, kbase);
    }
    
    protected StatelessKnowledgeSession prepareNewSession(KnowledgeBuilder kbuilder, KnowledgeBase kbase) {
        // Check the builder for errors
        if (kbuilder.hasErrors()) {
            System.out.println(kbuilder.getErrors().toString());
            throw new RuntimeException("Unable to compile drl\".");
        }
 
        // get the compiled packages (which are serializable)
        Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
 
        // add the packages to a knowledgebase (deploy the knowledge packages).
        kbase.addKnowledgePackages(pkgs);
 
        StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
        
        return ksession;
    }
    
    protected void fireRules(StatelessKnowledgeSession ksession, 
                             Collection collection) {
        
        int executionsCounter = 0;
        
        List<String> results = retrieveResultsList(collection);
        
        executionsCounter++;
        results.add("\n\nExecution " + executionsCounter + "\n\n");
        ksession.execute(collection);
        
        addToCollection("Hello", collection);
        executionsCounter++;
        results.add("\n\nExecution " + executionsCounter + "\n\n");
        ksession.execute(collection);
        
        /*ksession.fireAllRules();
        
        //initMessageObject();
        //initMessageObject("Hello");
        //initMessageObjects();
        
        ksession.fireAllRules();
        
        ksession.dispose();*/
    }
    
    protected Collection initCollection() {
        List messages = new ArrayList();
        List results = new ArrayList();
        
        messages.add(results);
        
        Message msg = new Message();
        msg.setType("Test");
        
        messages.add(msg);
        
        msg = new Message();
        msg.setType("Hello");
        
        messages.add(msg);
        
        //ksession.insert(messages);
        
        return messages;
    }
    
    protected void addToCollection(String type, Collection collection) {
        Message msg = new Message();
        msg.setType(type);
        
        ((List) collection).add(msg);
    }

    protected void logResults(Collection collection) {
        List<String> results = retrieveResultsList(collection);
                
        for (String result : results) {
            logger.info(result);
        }
    }
    
    protected List<String> retrieveResultsList(Collection collection) {
        ArrayList<String> results = null;
        
        for (Object object : collection) {
            if (object instanceof ArrayList) {
                results = (ArrayList<String>) object;
            }
        }
        
        return results;
    }

    protected void resetResults(Collection collection) {
        List<String> results = retrieveResultsList(collection);
        
        results.clear();
    }
}