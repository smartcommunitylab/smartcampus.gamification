package eu.trentorise.game.action.service;

import eu.trentorise.game.action.container.IActionContainer;
import eu.trentorise.game.action.container.IExternalActionContainer;
import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.model.Param;
import eu.trentorise.game.action.model.ParamType;
import eu.trentorise.game.action.response.ExternalActionResponse;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
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
@Service("mockActionManager")
public class MockActionManager extends MockResponder implements IActionManager {

    public static MockActionManager createInstance() {
        MockApplicationManager mockApplicationManager = MockApplicationManager.createInstance();
        MockActionManager mock = new MockActionManager();
        mock.setManager(mockApplicationManager);
        
        return mock;
    }
    
    @Override
    public ExternalActionResponse getExternalActions(IExternalActionContainer container) throws Exception {
        return this.makeExternalActionResponse(this.createActions());
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
    
    public List<ExternalAction> createActions() {
        List<ExternalAction> list = new ArrayList<>();
        
        Application application = manager.createViaggiaRovereto();
        
        list.add(this.createAction(application, 1, "BusDelayReporting", "The user has reported the delay of a bus"));
        list.add(this.createAction(application, 2, "BusServiceRating", "The user has rated the bus service quality"));
        list.add(this.createAction(application, 3, "BusCommentAdding", "The user has commented the bus service"));
        list.add(this.createItineratySavingExternalAction());
        list.add(this.createAction(application, 5, "BikeUsage", "The user has used a bike"));
        list.add(this.createAction(application, 6, "CarUsage", "The user has used a car"));
        list.add(this.createAction(application, 7, "BusUsage", "The user has used a bus"));
        
        return list;
    }
    
    protected ExternalAction createAction(Application application, Integer id, 
                                          String name, String description) {
        
        ExternalAction element = new ExternalAction();
        
        element = (ExternalAction) this.createAction(element, id, name, description);
        
        element.setApplication(application);
        
        return element;
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
    
    protected ExternalActionResponse makeExternalActionResponse(List<ExternalAction> list) {
        ExternalActionResponse response = new ExternalActionResponse();
        response.setActions(list);
        
        return ((ExternalActionResponse) this.buildPositiveResponse(response));
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
}