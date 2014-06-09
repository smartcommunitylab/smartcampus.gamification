package eu.trentorise.utils.drools.io;

import java.io.IOException;
import java.io.InputStream;
import org.kie.api.io.Resource;
import org.kie.internal.io.ResourceFactory;


public class ClassPathResourceDAO implements IResourceDAO {
   
    @Override
    public Resource getResource(String classPathResource) {
        return ResourceFactory.newClassPathResource(classPathResource);
    }

    @Override
    public InputStream getResourceStream(String classPathResource) throws IOException {
        return this.getResource(classPathResource).getInputStream();
        
        //File file = new File("/conf/drools/rules/basicBadge.drt");
        //return new FileInputStream(file);
        
        //return ResourceFactory.newClassPathResource("org/drools/examples/templates/Cheese.drt").getInputStream();
    }
}