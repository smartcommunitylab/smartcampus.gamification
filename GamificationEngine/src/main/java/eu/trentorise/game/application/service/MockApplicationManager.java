package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IExternalActionContainer;
import eu.trentorise.game.application.model.Application;
import eu.trentorise.game.application.model.ExternalAction;
import eu.trentorise.game.application.response.ExternalActionResponse;
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
    public ExternalActionResponse getExternalActions(IExternalActionContainer container) throws Exception {
        return this.makeResponse(this.createActions());
    }
    
    public List<ExternalAction> createActions() {
        List<ExternalAction> list = new ArrayList<>();
        
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
    
    protected ExternalAction createAction(Application application, Integer id, 
                                          String name, String description) {
        
        ExternalAction element = new ExternalAction();
        
        element.setApplication(application);
        element.setId(id);
        element.setName(name);
        element.setDescription(description);
        
        return element;
    }
    
    protected ExternalAction createItineratySavingAction() {
        return this.createAction(this.createApplication(), 4, "ItenerarySaving", "The user has saved an interary");
    }
    
    protected ExternalActionResponse makeResponse(List<ExternalAction> list) {
        ExternalActionResponse response = new ExternalActionResponse();
        response.setActions(list);
        
        return ((ExternalActionResponse) this.buildPositiveResponse(response));
    }
}