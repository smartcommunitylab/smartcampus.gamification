package eu.trentorise.game.action.service;

import eu.trentorise.game.action.comparator.ExternalActionKeyFkComparator;
import eu.trentorise.game.action.container.IImportExternalActionContainer;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.application.comparator.ApplicationKeyComparator;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockExternalActionManager")
public class MockExternalActionManager implements IExternalActionImporter,
                                                  IRestCrudManager<ExternalAction, Application, ExternalAction>,
                                                  IRestCrudTestManager<ExternalAction, Application, ExternalAction> {

    public static MockExternalActionManager createInstance() {
        MockExternalActionManager mock = new MockExternalActionManager();
        mock.mockActionManager = MockActionManager.createInstance();
        mock.mockApplicationManager = MockApplicationManager.createInstance();
        
        mock.comparator = new ExternalActionKeyFkComparator();
        ((ExternalActionKeyFkComparator) mock.comparator).setActionKeyComparator(mock.mockActionManager.getComparator());
        ((ExternalActionKeyFkComparator) mock.comparator).setApplicationKeyComparator(new ApplicationKeyComparator());
        
        return mock;
    }
    
    
    //CREATE - CUSTOM METHODS
    @Override
    public Collection<ExternalAction> importExternalActions(IImportExternalActionContainer container) throws Exception {
        //TODO: return null or throw Exception if it is not possible to 
        //complete this activity
        
        return this.createExternalActions(container);
    }
    
    
    //CRUD METHODS
    @Override
    public ExternalAction createSingleElement(ExternalAction containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Collection<ExternalAction> readCollection(Application containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella ExternalAction e recupera tutti i
        //gli elementi per l'app indicata
        return this.createElements(containerWithIds);
    }

    @Override
    public ExternalAction readSingleElement(ExternalAction containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        ExternalAction returnValue = null;
        
        ExternalAction expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public ExternalAction updateSingleElement(ExternalAction containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ExternalAction deleteSingleElement(ExternalAction containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        ExternalAction returnValue = null;
        
        ExternalAction expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public ExternalAction createElement(ExternalAction containerWithIds) throws Exception {
        return mockActionManager.createItineratySavingExternalAction();
    }
    
    @Override
    public Collection createElements(Application containerWithIds) throws Exception {
        return this.createViaggiaRoveretoExternalActions();
    }
    
    
    public Collection<ExternalAction> createExternalActions(IImportExternalActionContainer container) {
        return this.createViaggiaRoveretoExternalActions();
    }

    
    public List<ExternalAction> createViaggiaRoveretoExternalActions() {
        List<ExternalAction> list = new ArrayList<>();
        
        Application application = mockApplicationManager.createViaggiaRovereto();
        
        list.add(mockActionManager.createAction(application, 1, "BusDelayReporting", "The user has reported the delay of a bus"));
        list.add(mockActionManager.createAction(application, 2, "BusServiceRating", "The user has rated the bus service quality"));
        list.add(mockActionManager.createAction(application, 3, "BusCommentAdding", "The user has commented the bus service"));
        list.add(mockActionManager.createItineratySavingExternalAction());
        list.add(mockActionManager.createAction(application, 5, "BikeUsage", "The user has used a bike"));
        list.add(mockActionManager.createAction(application, 6, "CarUsage", "The user has used a car"));
        list.add(mockActionManager.createAction(application, 7, "BusUsage", "The user has used a bus"));
        
        return list;
    }
    
    public Comparator<ExternalAction> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockActionManager")
    @Autowired
    protected MockActionManager mockActionManager;
    
    
    @Qualifier("externalActionKeyFkComparator")
    @Autowired
    protected Comparator<ExternalAction> comparator;
    
    @Qualifier("mockApplicationManager")
    @Autowired
    protected MockApplicationManager mockApplicationManager;
}