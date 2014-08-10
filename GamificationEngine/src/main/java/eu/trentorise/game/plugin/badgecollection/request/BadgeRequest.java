package eu.trentorise.game.plugin.badgecollection.request;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public class BadgeRequest {
    
    protected BadgeCollectionPlugin badgeCollection;

    public BadgeCollectionPlugin getBadgeCollection() {
        return badgeCollection;
    }

    public void setBadgeCollection(BadgeCollectionPlugin badgeCollection) {
        this.badgeCollection = badgeCollection;
    }

    @Override
    public String toString() {
        return "BadgeRequest{" + "badgeCollection=" + badgeCollection + '}';
    }
}