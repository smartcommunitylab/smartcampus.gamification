package eu.trentorise.game.application.service;

import eu.trentorise.game.application.container.IBasicParamContainer;
import eu.trentorise.game.application.response.OperatorsResponse;

/**
 *
 * @author Luca Piras
 */
public interface IParamManager {

    public OperatorsResponse getOperatorsSupported(IBasicParamContainer container) throws Exception;
}