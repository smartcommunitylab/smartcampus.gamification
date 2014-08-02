package eu.trentorise.game.application.response;

import eu.trentorise.game.action.model.Application;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class ApplicationCollectionResponse {
    
    protected Collection<Application> applications;

    public Collection<Application> getApplications() {
        return applications;
    }

    public void setApplications(Collection<Application> applications) {
        this.applications = applications;
    }

    @Override
    public String toString() {
        return "ApplicationCollectionResponse{" + "applications=" + applications + '}';
    }
}