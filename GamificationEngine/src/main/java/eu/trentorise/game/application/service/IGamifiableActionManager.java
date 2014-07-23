package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IExternalActionContainer;
import eu.trentorise.game.application.response.ParamResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGamifiableActionManager {

    ParamResponse getParams(IExternalActionContainer container);
}