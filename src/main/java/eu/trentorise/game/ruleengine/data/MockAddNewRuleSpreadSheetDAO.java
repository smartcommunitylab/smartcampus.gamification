package eu.trentorise.game.ruleengine.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author Luca Piras
 */
@Repository("mockAddNewRuleSpreadSheetDAO")
public class MockAddNewRuleSpreadSheetDAO implements ISpreadSheetDAO {
    //TODO: deactivate this one because is only a mock
    private static final Logger logger = LoggerFactory.getLogger(MockAddNewRuleSpreadSheetDAO.class.getName());
    
    @Override
    public String getSpreadSheet() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"3\",\"1000\",\"Advanced Mayor\"\n");
        
        if (logger.isDebugEnabled()) {
            logger.debug("**************spreadSheet: " + sb);
        }
        
        return sb.toString();
    }
    
    /*protected String buildSpreadSheetHeader() {
        return "\"Case\",\"RuleId\",\"Points\",\"BadgeTitle\"\n";
    }*/
    
    /*protected InputStream getSpreadSheetStream() throws IOException {
        StringBuilder sb = new StringBuilder(buildSpreadSheetHeader());
        sb.append("\"Basic badge\",\"1\",\"10\",\"Basic Mayor\"\n");
        sb.append("\"Enhanced badge\",\"2\",\"100\",\"Enhanced Mayor\"\n");
        if (logger.isDebugEnabled()) {
            logger.debug("**************spreadSheet: " + sb);
        }
        
        return spreadSheetToStream(sb.toString());
    }*/
}