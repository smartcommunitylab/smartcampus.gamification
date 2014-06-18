package eu.trentorise.game.configuration.service;

import eu.trentorise.game.configuration.response.ImportGamifiableActionsResponse;
import eu.trentorise.game.profile.game.model.GameConfiguration;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameConfigurationManager {
    
    public ImportGamifiableActionsResponse importGamifiableActions(GameConfiguration gameConfiguration);
    
    public GameResponse activateDeactivatePlugin(GameConfiguration gameConfiguration);
}