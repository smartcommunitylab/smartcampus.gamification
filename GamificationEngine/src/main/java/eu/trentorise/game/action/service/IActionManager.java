package eu.trentorise.game.action.service;

import eu.trentorise.game.action.container.IActionContainer;
import eu.trentorise.game.action.response.ParamResponse;

/**
 *
 * @author Luca Piras
 */
public interface IActionManager {
    
    public ParamResponse getActionParams(IActionContainer container);
}