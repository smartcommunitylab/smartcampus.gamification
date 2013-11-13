package eu.trentorise.game.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import eu.trentorise.game.model.Player;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

@Service
public class GameManager implements IGameManager {

    private static Logger logger = LoggerFactory.getLogger(GameManager.class.getName());
    
    @Override
    public void getGame() {
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
 
        // read rules from String
        String myRule = this.buildBadgeRule("Basic badge", "10", "Basic Mayor");
        addRule(myRule, kbuilder);
        
        myRule = this.buildBadgeRule("Enhanced badge", "100", "Enhanced Mayor");
        addRule(myRule, kbuilder);
        
        return prepareNewSession(kbuilder, kbase);
    }
    
    protected String buildBadgeRule(String ruleName, String points, 
                                    String badgeTitle) {
        
        StringBuilder sb = new StringBuilder("import eu.trentorise.game.model.Player ");
        sb.append("import eu.trentorise.game.model.Badge ");
        sb.append("import java.util.ArrayList ");
        sb.append("rule \")").append(ruleName).append("\" ");
        sb.append("when player:Player(points==\"").append(points).append("\") ");
        sb.append("then Badge badge = new Badge(); ");
        sb.append("badge.setTitle(\"").append(badgeTitle).append("\"); ");
        sb.append("player.getBadges().add(badge); end");
        
        return sb.toString();
    }
    
        
    protected void addRule(String rule, KnowledgeBuilder kbuilder) {
        Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(rule));
        kbuilder.add(myResource, ResourceType.DRL);
    }
 
    protected void addNewRule(KnowledgeBuilder kbuilder, KnowledgeBase kbase) {
        
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
        Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
 
        // add the packages to a knowledgebase (deploy the knowledge packages).
        kbase.addKnowledgePackages(pkgs);
 
        StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
        
        return ksession;
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
        
        /*Badge badge = new Badge();
        badge.setTitle("Basic Mayor");
        badge.setNecessaryPoints(new Integer(10));
        
        elements.add(badge);
        
        badge = new Badge();
        badge.setTitle("Enhanced Mayor");
        badge.setNecessaryPoints(new Integer(100));
        
        elements.add(badge);
        
        badge = new Badge();
        badge.setTitle("Advanced Mayor");
        badge.setNecessaryPoints(new Integer(1000));
        
        elements.add(badge);*/
        
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
            Player current = (Player) object;
                        
            logger.info("Player: " + current + " - Badges: " + 
                        current.getBadges());
        }
    }
}