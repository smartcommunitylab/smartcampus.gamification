package eu.trentorise.game.action.service;

import eu.trentorise.game.action.comparator.ActionKeyComparator;
import eu.trentorise.game.action.container.IActionContainer;
import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.model.Param;
import eu.trentorise.game.action.model.ParamType;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.response.MockResponder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockActionManager")
public class MockActionManager extends MockResponder implements IActionManager {

    public static MockActionManager createInstance() {
        MockApplicationManager mockApplicationManager = MockApplicationManager.createInstance();
        MockActionManager mock = new MockActionManager();
        mock.setManager(mockApplicationManager);
        
        mock.comparator = new ActionKeyComparator();
        
        return mock;
    }
    
    @Override
    public ParamResponse getActionParams(IActionContainer container) {
        return this.makeParamResponse(this.createElements());
    }
    
    public List<Param> createElements() {
        List<Param> list = new ArrayList<>();
        
        Action action = this.createExternalAction();
        
        list.add(this.createBikeKmParam());
        list.add(this.createElement(action, "carKM", ParamType.INTEGER));
        list.add(this.createElement(action, "busKM", ParamType.INTEGER));
        list.add(this.createElement(action, "means", ParamType.INTEGER));
        
        return list;
    }
    
    protected Action createAction(Action element, Integer id, 
                                  String name, String description) {
        
        element.setId(id);
        element.setName(name);
        element.setDescription(description);
        
        return element;
    }
    
    protected ExternalAction createItineratySavingExternalAction() {
        return this.createAction(manager.createViaggiaRovereto(), 4, "ItenerarySaving", "The user has saved an interary");
    }
    
    public Action createItineratySavingAction() {
        Action element = new Action();
        return this.createAction(element, 4, "ItenerarySaving", "The user has saved an interary");
    }
    
    public ExternalAction createExternalAction() {
        return this.createItineratySavingExternalAction();
    }
    
    protected ExternalAction createAction(Application application, Integer id, 
                                          String name, String description) {
        
        ExternalAction element = new ExternalAction();
        
        element = (ExternalAction) this.createAction(element, id, name, description);
        
        element.setApplication(application);
        
        return element;
    }
    
    public Action createAction() {
        return this.createItineratySavingAction();
    }
    
    protected BasicParam createElement(Action action, String name, ParamType type) {
        BasicParam element = new BasicParam();
        element.setAction(action);
        element.setName(name);
        element.setType(ParamType.INTEGER);
        
        return element;
    }
    
    public BasicParam createBikeKmParam() {
        return this.createElement(this.createExternalAction(), "bikeKM", ParamType.INTEGER);
    }
    
    protected ParamResponse makeParamResponse(List<Param> list) {
        ParamResponse response = new ParamResponse();
        response.setParams(list);
        
        return ((ParamResponse) this.buildPositiveResponse(response));
    }

    
    public Comparator<Action> getComparator() {
        return comparator;
    }
    
    public MockApplicationManager getManager() {
        return manager;
    }
    
    public void setManager(MockApplicationManager manager) {
        this.manager = manager;
    }
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected MockApplicationManager manager;
    
    
    @Qualifier("actionKeyComparator")
    @Autowired
    protected Comparator<Action> comparator;
}