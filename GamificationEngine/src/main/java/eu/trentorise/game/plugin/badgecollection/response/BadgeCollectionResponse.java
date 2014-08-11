package eu.trentorise.game.plugin.badgecollection.response;

import eu.trentorise.game.plugin.badgecollection.model.Badge;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionResponse {
    
    protected Collection<Badge> badges;

    public Collection<Badge> getBadges() {
        return badges;
    }

    public void setBadges(Collection<Badge> badges) {
        this.badges = badges;
    }

    @Override
    public String toString() {
        return "BadgeCollectionResponse{" + "badges=" + badges + '}';
    }
}