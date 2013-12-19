package eu.trentorise.game.ruleengine.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Luca Piras
 */
public abstract class TemplateRulesDAO extends AbstractRulesDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(TemplateRulesDAO.class.getName());
    
    protected ISpreadSheetDAO spreadSheetDAO;
    
    protected IRulesStreamDAO rulesStreamDAO;
    
    
    @Override
    protected List<String> obtainsContentRules(List<String> contentRules) {
        String contentRule = null;
        
        String spreadSheet = spreadSheetDAO.getSpreadSheet();
        
        InputStream rulesStream = null;
        try {
            rulesStream = rulesStreamDAO.getRulesStream();
            
            contentRule = this.compile(spreadSheet, rulesStream);
        } catch (IOException e) {
            this.closeStream(rulesStream);
            
            throw new IllegalArgumentException("Could not read spreadsheet or rules stream.", e);
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
    
    protected abstract String compile(String spreadSheet, InputStream rulesStream);

    
    public ISpreadSheetDAO getSpreadSheetDAO() {
        return spreadSheetDAO;
    }

    public void setSpreadSheetDAO(ISpreadSheetDAO spreadSheetDAO) {
        this.spreadSheetDAO = spreadSheetDAO;
    }

    public IRulesStreamDAO getRulesStreamDAO() {
        return rulesStreamDAO;
    }

    public void setRulesStreamDAO(IRulesStreamDAO rulesStreamDAO) {
        this.rulesStreamDAO = rulesStreamDAO;
    }
}