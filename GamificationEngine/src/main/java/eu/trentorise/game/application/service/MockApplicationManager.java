package eu.trentorise.game.application.service;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.response.MockResponder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service("mockApplicationManager")
public class MockApplicationManager extends MockResponder implements IApplicationManager {

    public static MockApplicationManager createInstance() {
        return new MockApplicationManager();
    }

    @Override
    public ApplicationResponse getApplications() {
        return this.makeResponse(this.createElements());
    }
    
    public List<Application> createElements() {
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
    
    protected ApplicationResponse makeResponse(List<Application> list) {
        ApplicationResponse response = new ApplicationResponse();
        response.setApplications(list);
        
        return ((ApplicationResponse) this.buildPositiveResponse(response));
    }
}