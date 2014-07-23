package eu.trentorise.game.application.container;

import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.application.model.ExternalAction;

/**
 *
 * @author Luca Piras
 */
public interface IExternalActionContainer {
    
    public Action getAction();
    public void setAction(ExternalAction action);
}