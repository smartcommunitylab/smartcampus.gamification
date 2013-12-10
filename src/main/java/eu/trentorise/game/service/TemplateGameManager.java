package eu.trentorise.game.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.drools.core.io.impl.ByteArrayResource;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.decisiontable.InputType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.io.ResourceFactory;


public class TemplateGameManager extends GameManager {

    @Override
    protected void addRules(KnowledgeBuilder kbuilder) {
        this.addRules(kbuilder, null);
    }
    
    protected void addRules(KnowledgeBuilder kbuilder, 
                            InputStream spreadSheetStream) {
        
        //first we compile the decision table into a whole lot of rules.
        
        final ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();

        //the data we are interested in starts at row 2, column 2 (e.g. B2)
        String drl = null;
        try {
            if (null == spreadSheetStream) {
                spreadSheetStream = getSpreadSheetStream();
            }
            
            drl = converter.compile(spreadSheetStream, getRulesStream(), InputType.CSV, 2, 2);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read spreadsheet or rules stream." ,e);
        }
        System.out.println(drl);
        
        //DecisionTableConfiguration conf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        //conf.setInputType(DecisionTableInputType.CSV);

        //compile the drl
        kbuilder.add(new ByteArrayResource(drl.getBytes()), ResourceType.DRL);
    }
    
    @Override
    protected void addNewRule(KnowledgeBuilder kbuilder, KnowledgeBase kbase) throws IOException {
        StringBuilder sb = new StringBuilder(buildSpreadSheetHeader());
        sb.append("\"Advanced badge\",\"3\",\"1000\",\"Advanced Mayor\"\n");
        
        addRules(kbuilder, spreadSheetToStream(sb.toString()));
        
        prepareNewSession(kbuilder, kbase);
    }
    
    protected String buildSpreadSheetHeader() {
        return "\"Case\",\"RuleId\",\"Points\",\"BadgeTitle\"\n";
    }
    protected InputStream getSpreadSheetStream() throws IOException {
        StringBuilder sb = new StringBuilder(buildSpreadSheetHeader());
        sb.append("\"Basic badge\",\"1\",\"10\",\"Basic Mayor\"\n");
        sb.append("\"Enhanced badge\",\"2\",\"100\",\"Enhanced Mayor\"\n");
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