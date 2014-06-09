package eu.trentorise.game.profile.service;

import eu.trentorise.game.profile.model.GameConfiguration;
import eu.trentorise.game.response.SuccessResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameProfileManager {
    
    public SuccessResponse activateDeactivatePlugin(GameConfiguration gameConfiguration);
}