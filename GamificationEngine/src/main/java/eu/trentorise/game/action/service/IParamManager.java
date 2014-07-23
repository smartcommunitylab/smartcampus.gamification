package eu.trentorise.game.action.service;

import eu.trentorise.game.action.container.IBasicParamContainer;
import eu.trentorise.game.action.response.OperatorsResponse;

/**
 *
 * @author Luca Piras
 */
public interface IParamManager {

    public OperatorsResponse getOperatorsSupported(IBasicParamContainer container) throws Exception;
}