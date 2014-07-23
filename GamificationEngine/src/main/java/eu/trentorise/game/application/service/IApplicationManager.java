package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IExternalActionContainer;
import eu.trentorise.game.application.response.ExternalActionResponse;

/**
 *
 * @author Luca Piras
 */
public interface IApplicationManager {

    public ExternalActionResponse getExternalActions(IExternalActionContainer container) throws Exception;
}