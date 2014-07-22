package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IActionContainer;
import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.application.model.BasicParam;
import eu.trentorise.game.application.model.Param;
import eu.trentorise.game.application.model.ParamType;
import eu.trentorise.game.application.response.ParamResponse;
import eu.trentorise.game.response.MockResponder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGamifiableActionManager")
public class MockGamifiableActionManager extends MockResponder implements IGamifiableActionManager {
    
    @Override
    public ParamResponse getParams(IActionContainer container) {
        return this.makeResponse(this.createElements());
    }
    
    public List<Param> createElements() {
        List<Param> list = new ArrayList<>();
        
        Action action = this.createAction();
        
        list.add(this.createElement(action, "bikeKM", ParamType.INTEGER));
        list.add(this.createElement(action, "carKM", ParamType.INTEGER));
        list.add(this.createElement(action, "busKM", ParamType.INTEGER));
        list.add(this.createElement(action, "means", ParamType.INTEGER));
        
        return list;
    }
    
    public Action createAction() {
        return manager.createItineratySavingAction();
    }
    
    protected Param createElement(Action action, String name, ParamType type) {
        BasicParam element = new BasicParam();
        element.setAction(action);
        element.setName(name);
        element.setType(ParamType.INTEGER);
        
        return element;
    }
    
    protected ParamResponse makeResponse(List<Param> list) {
        ParamResponse response = new ParamResponse();
        response.setParams(list);
        
        return ((ParamResponse) this.buildPositiveResponse(response));
    }

    public void setManager(MockApplicationManager manager) {
        this.manager = manager;
    }
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected MockApplicationManager manager;
}