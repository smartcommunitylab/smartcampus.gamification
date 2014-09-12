package eu.trentorise.game.ruleengine.data.drools;

import eu.trentorise.game.ruleengine.data.RuleTemplateDAO;
import eu.trentorise.utils.io.IStringToStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.decisiontable.InputType;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 * @author Luca Piras
 */
public class DroolsRuleTemplateDAO extends RuleTemplateDAO {

    @Override
    protected String compile(String spreadSheet, InputStream rulesStream) {
        InputStream spreadSheetStream = null;
        
        //first we compile the decision table into a whole lot of rules.
        final ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        
        String drl = null;
        try {
            spreadSheetStream = getSpreadSheetStream(spreadSheet);
            
            //the data we are interested in starts at row 2, column 2 (e.g. B2)
            //drl = converter.compile(spreadSheetStream, getRulesStream(), InputType.CSV, 2, 2);
            drl = converter.compile(spreadSheetStream, rulesStream, InputType.CSV, 1, 1);
        } catch (IOException e) {
            super.closeStream(spreadSheetStream);
            
            throw new IllegalArgumentException("Could not read spreadsheet or rules stream.", e);
        } finally {
            super.closeStream(spreadSheetStream);
        }
        
        //DecisionTableConfiguration conf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        //conf.setInputType(DecisionTableInputType.CSV);

        //compile the drl
        //kbuilder.add(new ByteArrayResource(drl.getBytes()), ResourceType.DRL);
        
        return drl;
    }
    
    protected InputStream getSpreadSheetStream(String spreadSheet) throws UnsupportedEncodingException {
        return stringToStream.stringToStream(spreadSheet);
    }
    
    
    @Autowired
    protected IStringToStream stringToStream;
}