package eu.trentorise.game.application.container;

import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.application.model.ExternalAction;

/**
 *
 * @author Luca Piras
 */
public class ExternalActionContainer implements IExternalActionContainer {
    
    protected Action action;

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public void setAction(ExternalAction action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ExternalActionContainer{" + "action=" + action + '}';
    }
}