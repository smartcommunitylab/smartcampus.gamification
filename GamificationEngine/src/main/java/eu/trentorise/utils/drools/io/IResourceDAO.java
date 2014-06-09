package eu.trentorise.utils.drools.io;

import java.io.IOException;
import org.kie.api.io.Resource;
import java.io.InputStream;


/**
 *
 * @author Luca Piras
 */
public interface IResourceDAO {
    
    public Resource getResource(String resourcePath);
    
    public InputStream getResourceStream(String resourcePath) throws IOException;
}