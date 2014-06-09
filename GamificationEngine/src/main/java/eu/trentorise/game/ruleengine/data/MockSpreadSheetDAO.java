package eu.trentorise.game.ruleengine.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Luca Piras
 */
public class MockSpreadSheetDAO implements ISpreadSheetDAO {

    private static final Logger logger = LoggerFactory.getLogger(MockSpreadSheetDAO.class.getName());
    
    protected String content;
    
    @Override
    public String getSpreadSheet() {
        if (logger.isDebugEnabled()) {
            logger.debug("**************spreadSheet: " + this.content);
        }
        
        return this.content;
        
        /*StringBuilder sb = new StringBuilder();
        sb.append("\"1\",\"10\",\"Basic Mayor\"\n");
        sb.append("\"2\",\"100\",\"Enhanced Mayor\"\n");
        
        if (logger.isDebugEnabled()) {
            logger.debug("**************spreadSheet: " + sb);
        }
        
        return sb.toString();*/
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
    
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}