package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IActionContainer;
import eu.trentorise.game.application.response.ParamResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGamifiableActionManager {

    ParamResponse getParams(IActionContainer container);
}