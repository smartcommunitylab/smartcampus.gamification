package eu.trentorise.game.aaa.container;

import eu.trentorise.game.aaa.model.User;

/**
 *
 * @author Luca Piras
 */
public class AuthenticationContainer implements IAuthenticationContainer {
    
    protected User user;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}