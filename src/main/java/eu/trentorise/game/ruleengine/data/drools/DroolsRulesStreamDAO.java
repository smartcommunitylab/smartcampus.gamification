package eu.trentorise.game.ruleengine.data.drools;

import eu.trentorise.game.ruleengine.data.IRulesStreamDAO;
import java.io.IOException;
import java.io.InputStream;
import org.kie.api.io.Resource;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Repository;

//TODO: evaluate if is the case to refactoring it and his interface moving them
//in something as eu.trentorise.utils.drools.io
@Repository("droolsRulesStreamDAO")
public class DroolsRulesStreamDAO implements IRulesStreamDAO {

    @Override
    public InputStream getRulesStream() throws IOException {
        //TODO: generalize it and passing it with a parameter
        Resource newClassPathResource = ResourceFactory.newClassPathResource("/conf/drools/rules/basicBadge.drt");
        
        return newClassPathResource.getInputStream();
        
        //File file = new File("/conf/drools/rules/basicBadge.drt");
        //return new FileInputStream(file);
        
        //return ResourceFactory.newClassPathResource("org/drools/examples/templates/Cheese.drt").getInputStream();
    }
}