package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.rule.Rule;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.decisiontable.InputType;
import org.kie.api.io.Resource;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author luca
 */
@Repository("templateRulesDAO")
public class TemplateRulesDAO implements IRulesDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(TemplateRulesDAO.class.getName());
    
    @Override
    public List<Rule> getRules(Integer gamificationApproachId) {
        InputStream spreadSheetStream = null;
        
        List<Rule> rules = new ArrayList<>();
        
        //first we compile the decision table into a whole lot of rules.
        
        final ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();

        //the data we are interested in starts at row 2, column 2 (e.g. B2)
        String drl = null;
        try {
            if (null == spreadSheetStream) {
                spreadSheetStream = getSpreadSheetStream();
            }
            
            //drl = converter.compile(spreadSheetStream, getRulesStream(), InputType.CSV, 2, 2);
            drl = converter.compile(spreadSheetStream, getRulesStream(), InputType.CSV, 1, 1);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read spreadsheet or rules stream." ,e);
        }
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("************DRL: ");
            sb.append(drl);
            logger.debug(sb.toString());
        }
        
        //DecisionTableConfiguration conf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        //conf.setInputType(DecisionTableInputType.CSV);

        //compile the drl
        //kbuilder.add(new ByteArrayResource(drl.getBytes()), ResourceType.DRL);
        
        
        Rule rule = new Rule("1", null, drl);
        rules.add(rule);
        
        return rules;
    }
    
    /*protected String buildSpreadSheetHeader() {
        return "\"Case\",\"RuleId\",\"Points\",\"BadgeTitle\"\n";
    }*/
    
    /*protected InputStream getSpreadSheetStream() throws IOException {
        StringBuilder sb = new StringBuilder(buildSpreadSheetHeader());
        sb.append("\"Basic badge\",\"1\",\"10\",\"Basic Mayor\"\n");
        sb.append("\"Enhanced badge\",\"2\",\"100\",\"Enhanced Mayor\"\n");
        System.out.println(sb);
        
        return spreadSheetToStream(sb.toString());
    }*/
    
    protected InputStream getSpreadSheetStream() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("\"1\",\"10\",\"Basic Mayor\"\n");
        sb.append("\"2\",\"100\",\"Enhanced Mayor\"\n");
        System.out.println(sb);
        
        return spreadSheetToStream(sb.toString());
    }
    
    protected InputStream spreadSheetToStream(String spreadSheet) throws IOException {
        InputStream is = new ByteArrayInputStream(spreadSheet.getBytes("UTF-8"));
        
        return is;
    }

    protected InputStream getRulesStream() throws IOException {
        Resource newClassPathResource = ResourceFactory.newClassPathResource("/conf/drools/rules/basicBadge.drt");
        
        return newClassPathResource.getInputStream();
        
        //File file = new File("/conf/drools/rules/basicBadge.drt");
        //return new FileInputStream(file);
        
        //return ResourceFactory.newClassPathResource("org/drools/examples/templates/Cheese.drt").getInputStream();
    }
}