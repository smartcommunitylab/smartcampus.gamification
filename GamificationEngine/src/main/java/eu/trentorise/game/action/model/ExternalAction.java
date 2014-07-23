package eu.trentorise.game.action.model;

/**
 *
 * @author Luca Piras
 */
public class ExternalAction extends Action {
    
    protected Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "ExternalAction{" + "application=" + application + " " + super.toString() + " " + '}';
    }
}