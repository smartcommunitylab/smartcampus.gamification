package eu.trentorise.game.application.container;

import eu.trentorise.game.application.model.Action;

/**
 *
 * @author Luca Piras
 */
public class ActionContainer implements IActionContainer {
    
    protected Action action;

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ActionContainer{" + "action=" + action + '}';
    }
}