package eu.trentorise.game.request;

import eu.trentorise.game.model.backpack.Badge;

/**
 *
 * @author Luca Piras
 */
public class HelloGameRequest {    
    
    protected Badge badge;

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }
}