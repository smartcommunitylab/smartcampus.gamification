package eu.trentorise.game.action.response;

import eu.trentorise.game.action.model.ExternalAction;

/**
 *
 * @author Luca Piras
 */
public class ExternalActionResponse {
    
    protected ExternalAction externalAction;

    public ExternalAction getExternalAction() {
        return externalAction;
    }

    public void setExternalAction(ExternalAction externalAction) {
        this.externalAction = externalAction;
    }

    @Override
    public String toString() {
        return "ExternalActionResponse{" + "externalAction=" + externalAction + '}';
    }
}