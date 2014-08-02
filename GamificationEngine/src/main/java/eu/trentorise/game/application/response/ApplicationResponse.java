package eu.trentorise.game.application.response;

import eu.trentorise.game.action.model.Application;

/**
 *
 * @author Luca Piras
 */
public class ApplicationResponse {
    
    protected Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "ApplicationResponse{" + "application=" + application + '}';
    }
}