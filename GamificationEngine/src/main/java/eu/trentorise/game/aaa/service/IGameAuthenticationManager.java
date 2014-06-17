package eu.trentorise.game.aaa.service;

import eu.trentorise.game.aaa.co.GameAuthenticationCO;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameAuthenticationManager {
    
    public GameResponse authenticate(GameAuthenticationCO gameAuthentication);
}