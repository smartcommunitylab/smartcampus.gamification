package eu.trentorise.game.service;

import eu.trentorise.game.model.backpack.Badge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.trentorise.game.model.player.Player;
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


public class GameManager implements IGameManager {

    private static Logger logger = LoggerFactory.getLogger(GameManager.class.getName());
    
    @Override
    public void getGame() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        
        StatelessKnowledgeSession ksession = initDrools(kbuilder, kbase);
        Collection collection = initCollection();
        fireRules(ksession, collection);
        
        logResults(collection);
        
        //resetResults(collection);
        collection = initCollection();
        
        addNewRule(kbuilder, kbase);
        fireRules(ksession, collection);
        
        logResults(collection);
    }
 
    protected StatelessKnowledgeSession initDrools(KnowledgeBuilder kbuilder, KnowledgeBase kbase) {
        this.addRules(kbuilder);
        
        return prepareNewSession(kbuilder, kbase);
    }
    
    protected void addRules(KnowledgeBuilder kbuilder) {
        // read rules from String
        String myRule = this.buildBadgeRule("Basic badge", "10", "Basic Mayor");
        this.addRule(myRule, kbuilder);
        
        myRule = this.buildBadgeRule("Enhanced badge", "100", "Enhanced Mayor");
        this.addRule(myRule, kbuilder);
    }
    
    protected String buildBadgeRule(String ruleName, String points, 
                                    String badgeTitle) {
        
        StringBuilder sb = new StringBuilder("import eu.trentorise.game.model.player.Player ");
        sb.append("import eu.trentorise.game.model.backpack.Badge ");
        sb.append("import java.util.ArrayList ");
        sb.append("rule \"").append(ruleName).append("\" ");
        sb.append("when player:Player(points==\"").append(points).append("\") ");
        sb.append("then Badge badge = new Badge(\"").append(badgeTitle).append("\", ").append(points).append("); ");
        sb.append("player.getBadges().add(badge); end");
        
        return sb.toString();
    }
        
    protected void addRule(String rule, KnowledgeBuilder kbuilder) {
        Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(rule));
        kbuilder.add(myResource, ResourceType.DRL);
    }
 
    protected void addNewRule(KnowledgeBuilder kbuilder, KnowledgeBase kbase) throws Exception {
        
        // read rules from String
        String myRule = this.buildBadgeRule("Advanced badge", "1000", "Advanced Mayor");
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
        Collection<KnowledgePackage> pkgs = this.getKnowledgePackages(kbuilder);
 
        // add the packages to a knowledgebase (deploy the knowledge packages).
        kbase.addKnowledgePackages(pkgs);
 
        StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
        
        return ksession;
    }
    
    protected Collection<KnowledgePackage> getKnowledgePackages(Object object) {
        return ((KnowledgeBuilder) object).getKnowledgePackages();
    }
    
    protected void fireRules(StatelessKnowledgeSession ksession, 
                             Collection collection) {
        
        //results.add("\n\nExecution " + executionsCounter + "\n\n");
        ksession.execute(collection);
        
        /*ksession.fireAllRules();
        
        //initMessageObject();
        //initMessageObject("Hello");
        //initMessageObjects();
        
        ksession.fireAllRules();
        
        ksession.dispose();*/
    }
    
    protected Collection initCollection() {
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
}