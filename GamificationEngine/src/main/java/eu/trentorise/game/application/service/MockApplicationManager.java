package eu.trentorise.game.application.service;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.utils.rest.ICrudManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("mockApplicationManager")
public class MockApplicationManager extends MockResponder implements IApplicationManager, ICrudManager<Application, Object, Application> {

    public static MockApplicationManager createInstance() {
        return new MockApplicationManager();
    }

    @Override
    public Application createSingleElement(Application containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it si not possible to create a
        //new one
        containerWithForeignIds.setId(0);
        return containerWithForeignIds;
    }
    
    @Override
    public Collection<Application> readCollection(Object container) throws Exception {
        return this.readApplications();
    }

    @Override
    public Application readSingleElement(Application container) throws Exception {
        return this.readApplicationById(container.getId());
    }
    
    @Override
    public Collection<Application> readApplications() throws Exception {
        return this.createElements();
    }
    
    @Override
    public Application readApplicationById(Integer appId) throws Exception {
        Application returnValue = null;
        
        Application app = new Application();
        app.setId(appId);
        
        Application expectedApp = this.createViaggiaRovereto();
        
        if (0 == comparator.compare(app, expectedApp)) {
            returnValue = expectedApp;
        }
        
        return returnValue;
    }
    
    public Collection<Application> createElements() {
        List<Application> elements = new ArrayList<>();
        
        elements.add(this.createViaggiaRovereto());
        
        Application application = new Application();
        application.setId(1);
        application.setName("VIAGGIATRENTO");
        elements.add(application);
        
        application = new Application();
        application.setName("VIVITRENTO");
        application.setId(2);
        elements.add(application);
        
        return elements;
    }
    
    public Application createViaggiaRovereto() {
        Application application = new Application();
        application.setId(0);
        application.setName("VIAGGIAROVERETO");
        return application;
    }
    
    @Qualifier("applicationKeyComparator")
    @Autowired
    protected Comparator<Application> comparator;
}