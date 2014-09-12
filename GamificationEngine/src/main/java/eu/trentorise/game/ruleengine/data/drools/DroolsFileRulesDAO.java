package eu.trentorise.game.ruleengine.data.drools;

import eu.trentorise.game.ruleengine.data.AbstractRulesDAO;
import eu.trentorise.game.ruleengine.data.IRulesStreamDAO;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Luca Piras
 */
public class DroolsFileRulesDAO extends AbstractRulesDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(DroolsFileRulesDAO.class.getName());
    
    protected IRulesStreamDAO rulesStreamDAO;
    
    
    @Override
    protected List<String> obtainsContentRules(List<String> contentRules) {
        String contentRule = null;

        InputStream rulesStream = null;
        try {
            rulesStream = rulesStreamDAO.getRulesStream();

            StringWriter writer = new StringWriter();
            IOUtils.copy(rulesStream, writer, "UTF-8");
            contentRule = writer.toString();
        } catch (IOException e) {
            this.closeStream(rulesStream);

            throw new IllegalArgumentException("Could not read rules stream.", e);
        } finally {
            this.closeStream(rulesStream);
        }

        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("************DRL: ");
            sb.append(contentRule);
            logger.debug(sb.toString());
        }

        contentRules.add(contentRule);
        
        return contentRules;
    }
    
    protected void closeStream(InputStream stream) {
        try {
            stream.close();
        } catch (Exception ex) {}
    }

    public IRulesStreamDAO getRulesStreamDAO() {
        return rulesStreamDAO;
    }

    public void setRulesStreamDAO(IRulesStreamDAO rulesStreamDAO) {
        this.rulesStreamDAO = rulesStreamDAO;
    }
}


    