package eu.trentorise.game.ruleengine.service;

import java.util.Collection;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.runtime.StatelessKnowledgeSession;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 *
 * @author Luca Piras
 */
@Service("droolsRulesExecutionManager")
public class DroolsRulesExecutionManager extends AbstractRulesExecutionManager<KnowledgeBase, StatelessKnowledgeSession> {

    public DroolsRulesExecutionManager() {
        super(LoggerFactory.getLogger(DroolsRulesExecutionManager.class.getName()));
    }
    
    @Override
    protected StatelessKnowledgeSession buildSession(IKnowledgeBuilder knowledgeBuilder) {
        /*
         * TODO: IMPORTANT!!! improve the approach with a refactoring using KIE
         * and configuring the project to use it for management of the session
         * and above all of the kbase (this is very heavy to build and KIE 
         * manage for you the caching mechanism - kbase very heavy and session
         * very light). Don't use the deprecated kbase.
         */
        
        /*TODO: IMPORTANT!!! To create every time the knowledgeBase is not good,
          use KIE!!!*/
        // get the compiled packages (which are serializable)
        Collection<KnowledgePackage> pkgs = ((Collection<KnowledgePackage>) knowledgeBuilder.getKnowledgePackages());
        
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        
        //TODO: verify if with drool the adding of knowledgePackages to a 
        //knoledgeBase is heavy
        
        // add the packages to a knowledgebase (deploy the knowledge packages).
        knowledgeBase.addKnowledgePackages(pkgs);
 
        return knowledgeBase.newStatelessKnowledgeSession();
    }

    @Override
    protected void fireRules(IKnowledgeBuilder knowledgeBuilder, 
                             Collection facts, 
                             StatelessKnowledgeSession knowledgeSession) {
        
        knowledgeSession.execute(facts);
    }
}