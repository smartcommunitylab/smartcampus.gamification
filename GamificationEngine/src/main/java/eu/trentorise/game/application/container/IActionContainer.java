package eu.trentorise.game.application.container;

import eu.trentorise.game.application.model.Action;

/**
 *
 * @author Luca Piras
 */
public interface IActionContainer {
    
    public Action getAction();
    public void setAction(Action action);
}