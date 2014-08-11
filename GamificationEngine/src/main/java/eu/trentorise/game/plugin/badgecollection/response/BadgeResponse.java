package eu.trentorise.game.plugin.badgecollection.response;

import eu.trentorise.game.plugin.badgecollection.model.Badge;

/**
 *
 * @author Luca Piras
 */
public class BadgeResponse {
    
    protected Badge badge;

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    @Override
    public String toString() {
        return "BadgeResponse{" + "badge=" + badge + '}';
    }
}