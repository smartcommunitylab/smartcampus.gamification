package eu.trentorise.game.action.request;

import eu.trentorise.game.action.model.ExternalAction;

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