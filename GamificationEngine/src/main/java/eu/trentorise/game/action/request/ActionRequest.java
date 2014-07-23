package eu.trentorise.game.action.request;

import eu.trentorise.game.action.model.Action;

/**
 *
 * @author Luca Piras
 */
public class ActionRequest {
    
    protected Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ActionRequest{" + "action=" + action + '}';
    }
}