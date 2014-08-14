package eu.trentorise.game.action.service;

import eu.trentorise.game.action.comparator.ActionKeyComparator;
import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.action.model.InternalAction;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockActionManager")
public class MockActionManager {

    public static MockActionManager createInstance() {
        MockActionManager mock = new MockActionManager();
        
        MockApplicationManager mockApplicationManager = MockApplicationManager.createInstance();
        mock.setManager(mockApplicationManager);
        
        mock.comparator = new ActionKeyComparator();
        
        return mock;
    }
    
    
    protected Action createAction(Action element, Integer id, 
                                  String name, String description) {
        
        element.setId(id);
        element.setName(name);
        element.setDescription(description);
        
        return element;
    }
    
    protected ExternalAction createItineratySavingExternalAction() {
        return this.createExternalAction(manager.createViaggiaRovereto(), 4, "ItinerarySaving", "The user has saved an itinerary");
    }
    
    public ExternalAction createBusDelayReportingAction() {
        Application app = this.manager.createViaggiaRovereto();
        return this.createExternalAction(app, 1, "BusDelayReporting", "The user has reported the delay of a bus");
    }
    
    public ExternalAction createItineratySavingAction() {
        Application app = this.manager.createViaggiaRovereto();
        return this.createExternalAction(app, 4, "ItinerarySaving", "The user has saved an itinerary");
    }
    
    public ExternalAction createExternalAction() {
        return this.createItineratySavingExternalAction();
    }
    
    protected ExternalAction createExternalAction(Application application, Integer id, 
                                          String name, String description) {
        
        ExternalAction element = new ExternalAction();
        
        element = (ExternalAction) this.createAction(element, id, name, description);
        
        element.setApplication(application);
        
        return element;
    }
    
    protected InternalAction createInternalAction(CustomizedPlugin customizedPlugin, 
                                                  RuleTemplate ruleTemplate,
                                                  Integer id, 
                                                  String name, 
                                                  String description) {
        
        InternalAction element = new InternalAction();
        
        element = (InternalAction) this.createAction(element, id, name, 
                                                     description);
        
        element.setCustomizedPlugin(customizedPlugin);
        element.setRuleTemplate(ruleTemplate);
        
        return element;
    }
    
    public Action createAction() {
        return this.createItineratySavingAction();
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