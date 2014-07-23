package eu.trentorise.game.action.service;

import eu.trentorise.game.action.container.IBasicParamContainer;
import eu.trentorise.game.action.response.OperatorsResponse;
import eu.trentorise.game.response.MockResponder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockParamManager")
public class MockParamManager extends MockResponder implements IParamManager {
    
    @Override
    public OperatorsResponse getOperatorsSupported(IBasicParamContainer container) throws Exception {
        return this.makeResponse(this.createElements());
    }
    
    public List<String> createElements() {
        List<String> list = new ArrayList<>();
        
        list.add(">");
        list.add(">=");
        list.add("<");
        list.add("<=");
        list.add("=");
        list.add("!=");
        
        return list;
    }
    
    protected OperatorsResponse makeResponse(List<String> list) {
        OperatorsResponse response = new OperatorsResponse();
        response.setOperators(list);
        
        return ((OperatorsResponse) this.buildPositiveResponse(response));
    }
}