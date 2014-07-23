package eu.trentorise.game.action.container;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.ExternalAction;

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