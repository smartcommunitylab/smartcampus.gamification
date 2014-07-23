package eu.trentorise.game.action.container;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.ExternalAction;

/**
 *
 * @author Luca Piras
 */
public interface IExternalActionContainer {
    
    public Action getAction();
    public void setAction(ExternalAction action);
}