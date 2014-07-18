package eu.trentorise.game.plugin.badgecollection.container;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;

/**
 *
 * @author Luca Piras
 */
public class BadgeContainer implements IBadgeContainer {
    
    protected BadgeCollectionPlugin badgeCollection;

    @Override
    public BadgeCollectionPlugin getBadgeCollection() {
        return badgeCollection;
    }

    @Override
    public void setBadgeCollection(BadgeCollectionPlugin badgeCollection) {
        this.badgeCollection = badgeCollection;
    }

    @Override
    public String toString() {
        return "BadgeContainer{" + "badgeCollection=" + badgeCollection + '}';
    }
}