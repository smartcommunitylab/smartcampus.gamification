package eu.trentorise.game.application.response;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.response.GameResponse;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class ApplicationResponse extends GameResponse {
    
    protected List<Application> applications;

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    @Override
    public String toString() {
        return "ApplicationResponse{" + "applications=" + applications + '}';
    }
}