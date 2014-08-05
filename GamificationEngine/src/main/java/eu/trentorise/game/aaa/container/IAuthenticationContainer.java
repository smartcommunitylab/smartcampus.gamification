package eu.trentorise.game.aaa.container;

import eu.trentorise.game.aaa.model.User;

/**
 *
 * @author Luca Piras
 */
public interface IAuthenticationContainer {
    
    public User getUser();
    public void setUser(User user);
}