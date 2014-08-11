package eu.trentorise.game.configuration.service;

import eu.trentorise.game.profile.game.model.GameConfiguration;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public interface IGameConfigurationManager {
    public GameResponse activateDeactivatePlugin(GameConfiguration gameConfiguration);
}