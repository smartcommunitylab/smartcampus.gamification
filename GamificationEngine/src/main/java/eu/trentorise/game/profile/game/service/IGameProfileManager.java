package eu.trentorise.game.profile.game.service;

import eu.trentorise.game.profile.game.response.NewGameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameProfileManager {
    
    public NewGameResponse newGame(Object object);
}