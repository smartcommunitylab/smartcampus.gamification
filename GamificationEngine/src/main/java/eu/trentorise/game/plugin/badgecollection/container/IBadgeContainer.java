package eu.trentorise.game.plugin.badgecollection.container;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;

/**
 *
 * @author Luca Piras
 */
public interface IBadgeContainer {
    
    public BadgeCollectionPlugin getBadgeCollection();
    public void setBadgeCollection(BadgeCollectionPlugin badgeCollection);
}