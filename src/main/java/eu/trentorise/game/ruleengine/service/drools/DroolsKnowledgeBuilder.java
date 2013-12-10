package eu.trentorise.game.ruleengine.service.drools;

import eu.trentorise.game.rule.Rule;
import eu.trentorise.game.ruleengine.service.IKnowledgeBuilder;
import java.io.Reader;
import java.io.StringReader;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;


/**
 * 
 * @author Luca Piras
 */
@Service("droolsKnowledgeBuilder")
public class DroolsKnowledgeBuilder implements IKnowledgeBuilder {

    protected KnowledgeBuilder knowledgeBuilder;

    public DroolsKnowledgeBuilder() {
        this.knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    }
    
    @Override
    public void addRule(Rule rule) {
        Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(rule.getContent()));
        this.knowledgeBuilder.add(myResource, ResourceType.DRL);
    }

    @Override
    public boolean hasErrors() {
        return this.knowledgeBuilder.hasErrors();
    }

    @Override
    public String getErrors() {
        return this.knowledgeBuilder.getErrors().toString();
    }

    @Override
    public Object getKnowledgePackages() {
        return this.knowledgeBuilder.getKnowledgePackages();
    }
}