package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IActionContainer;
import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.application.model.Application;
import eu.trentorise.game.application.response.ActionResponse;
import eu.trentorise.game.response.MockResponder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockApplicationManager")
public class MockApplicationManager extends MockResponder implements IApplicationManager {

    @Override
    public ActionResponse getActions(IActionContainer container) throws Exception {
        return this.makeResponse(this.createActions());
    }
    
    public List<Action> createActions() {
        List<Action> list = new ArrayList<>();
        
        Application application = this.createApplication();
        
        list.add(this.createAction(application, 1, "BusDelayReporting", "The user has reported the delay of a bus"));
        list.add(this.createAction(application, 2, "BusServiceRating", "The user has rated the bus service quality"));
        list.add(this.createAction(application, 3, "BusCommentAdding", "The user has commented the bus service"));
        list.add(this.createItineratySavingAction());
        list.add(this.createAction(application, 5, "BikeUsage", "The user has used a bike"));
        list.add(this.createAction(application, 6, "CarUsage", "The user has used a car"));
        list.add(this.createAction(application, 7, "BusUsage", "The user has used a bus"));
        
        return list;
    }
    
    public Application createApplication() {
        Application app = new Application();
        app.setId(0);
        return app;
    }
    
    protected Action createAction(Application application, Integer id, 
                                  String name, String description) {
        
        Action element = new Action();
        
        element.setApplication(application);
        element.setId(id);
        element.setName(name);
        element.setDescription(description);
        
        return element;
    }
    
    protected Action createItineratySavingAction() {
        return this.createAction(this.createApplication(), 4, "ItenerarySaving", "The user has saved an interary");
    }
    
    protected ActionResponse makeResponse(List<Action> list) {
        ActionResponse response = new ActionResponse();
        response.setActions(list);
        
        return ((ActionResponse) this.buildPositiveResponse(response));
    }
}