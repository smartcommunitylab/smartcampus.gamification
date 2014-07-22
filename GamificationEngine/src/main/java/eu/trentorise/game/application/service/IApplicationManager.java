package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IActionContainer;
import eu.trentorise.game.application.response.ActionResponse;

/**
 *
 * @author Luca Piras
 */
public interface IApplicationManager {

    public ActionResponse getActions(IActionContainer container) throws Exception;
}