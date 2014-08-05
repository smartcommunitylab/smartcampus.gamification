package eu.trentorise.game.aaa.service;

import eu.trentorise.game.aaa.container.IAuthenticationContainer;
import eu.trentorise.game.aaa.model.User;

/**
 *
 * @author Luca Piras
 */
public interface IGameAuthenticationManager {
    
    public User authenticate(IAuthenticationContainer container);
}