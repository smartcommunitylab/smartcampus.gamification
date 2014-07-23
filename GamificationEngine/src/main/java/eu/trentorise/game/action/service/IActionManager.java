package eu.trentorise.game.action.service;

import eu.trentorise.game.action.container.IActionContainer;
import eu.trentorise.game.action.container.IExternalActionContainer;
import eu.trentorise.game.action.response.ExternalActionResponse;
import eu.trentorise.game.action.response.ParamResponse;

/**
 *
 * @author Luca Piras
 */
public interface IActionManager {

    public ExternalActionResponse getExternalActions(IExternalActionContainer container) throws Exception;
    
    public ParamResponse getActionParams(IActionContainer container);
}