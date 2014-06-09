package eu.trentorise.game.ruleengine.data.drools;

import eu.trentorise.game.ruleengine.data.IRulesStreamDAO;
import eu.trentorise.utils.drools.io.IResourceDAO;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class DroolsRulesStreamDAO implements IRulesStreamDAO {

    protected String resourcePath;
    
    
    @Override
    public InputStream getRulesStream() throws IOException {
        return dao.getResourceStream(resourcePath);
    }

    
    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    
    @Qualifier("classPathResourceDAO")
    @Autowired
    protected IResourceDAO dao;
}