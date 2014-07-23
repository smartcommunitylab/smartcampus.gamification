package eu.trentorise.game.action.container;

import eu.trentorise.game.action.model.Action;

/**
 *
 * @author Luca Piras
 */
public interface IActionContainer {
    
    public Action getAction();
    public void setAction(Action action);
}