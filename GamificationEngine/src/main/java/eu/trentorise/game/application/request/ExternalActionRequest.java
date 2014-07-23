package eu.trentorise.game.application.request;

import eu.trentorise.game.application.model.ExternalAction;

/**
 *
 * @author Luca Piras
 */
public class ExternalActionRequest {
    
    protected ExternalAction action;

    public ExternalAction getAction() {
        return action;
    }

    public void setAction(ExternalAction action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ExternalActionRequest{" + "action=" + action + '}';
    }
}