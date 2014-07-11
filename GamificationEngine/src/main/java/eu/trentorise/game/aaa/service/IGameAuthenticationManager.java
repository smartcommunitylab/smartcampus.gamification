package eu.trentorise.game.aaa.service;

import eu.trentorise.game.aaa.request.GameAuthenticationRequest;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IGameAuthenticationManager {
    
    public GameResponse authenticate(GameAuthenticationRequest gameAuthentication);
}