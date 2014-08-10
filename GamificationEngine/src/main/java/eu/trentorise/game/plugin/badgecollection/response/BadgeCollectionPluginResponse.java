package eu.trentorise.game.plugin.badgecollection.response;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionPluginResponse {
    
    protected BadgeCollectionPlugin badgeCollectionPlugin;

    public BadgeCollectionPlugin getBadgeCollectionPlugin() {
        return badgeCollectionPlugin;
    }

    public void setBadgeCollectionPlugin(BadgeCollectionPlugin badgeCollectionPlugin) {
        this.badgeCollectionPlugin = badgeCollectionPlugin;
    }

    @Override
    public String toString() {
        return "BadgeCollectionPluginResponse{" + "badgeCollectionPlugin=" + badgeCollectionPlugin + '}';
    }
}