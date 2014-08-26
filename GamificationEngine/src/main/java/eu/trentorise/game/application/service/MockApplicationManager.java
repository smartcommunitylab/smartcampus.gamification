package eu.trentorise.game.application.service;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.comparator.ApplicationKeyComparator;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("mockApplicationManager")
public class MockApplicationManager implements IRestCrudManager<Application, Object, Application>,
                                               IRestCrudTestManager<Application, Object, Application> {

    public static MockApplicationManager createInstance() {
        MockApplicationManager mock = new MockApplicationManager();
        
        mock.comparator = new ApplicationKeyComparator();
        
        return mock;
    }

    
    //CREATE
    @Override
    public Application createSingleElement(Application containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        containerWithForeignIds.setId(0);
        return containerWithForeignIds;
    }
    
    
    //READ
    @Override
    public Collection<Application> readCollection(Object containerWithIds) throws Exception {
        return this.createElements(containerWithIds);
    }

    @Override
    public Application readSingleElement(Application containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        Application returnValue = null;
        
        Application expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public Application createElement(Application containerWithIds) throws Exception {
        return this.createViaggiaRovereto();
    }

    @Override
    public Collection<Application> createElements(Object containerWithIds) throws Exception {
        List<Application> elements = new ArrayList<>();
        
        elements.add(this.createViaggiaRovereto());
        
        /*Application application = new Application();
        application.setId(1);
        application.setName("VIAGGIATRENTO");
        elements.add(application);
        
        application = new Application();
        application.setName("VIVITRENTO");
        application.setId(2);
        elements.add(application);*/
        
        return elements;
    }
    
    
    //UPDATE
    @Override
    public Application updateSingleElement(Application containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        Application returnValue = null;
        
        Application expectedApp = this.createViaggiaRovereto();
        if (0 == comparator.compare(containerWithForeignIds, expectedApp)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }
    
    
    //DELETE
    @Override
    public Application deleteSingleElement(Application containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        Application returnValue = null;
        
        Application expectedApp = this.createViaggiaRovereto();
        if (0 == comparator.compare(containerWithIds, expectedApp)) {
            returnValue = expectedApp;
        }
        
        return returnValue;
    }
    
    
    public Application createViaggiaRovereto() {
        Application application = new Application();
        application.setId(1);
        application.setName("VIAGGIAROVERETO");
        return application;
    }

    
    public Comparator<Application> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("applicationKeyComparator")
    @Autowired
    protected Comparator<Application> comparator;
}