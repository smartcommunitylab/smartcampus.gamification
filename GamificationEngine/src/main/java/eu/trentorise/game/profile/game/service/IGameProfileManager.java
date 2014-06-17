package eu.trentorise.game.profile.game.service;

import eu.trentorise.game.profile.game.model.GameConfiguration;
import eu.trentorise.game.profile.game.response.NewGameResponse;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameProfileManager {
    
    public NewGameResponse newGame(Object object);
    
    public GameResponse activateDeactivatePlugin(GameConfiguration gameConfiguration);
}