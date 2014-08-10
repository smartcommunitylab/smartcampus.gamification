package eu.trentorise.game.plugin.badgecollection.response;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionPluginCollectionResponse {
    
    protected Collection<BadgeCollectionPlugin> badgeCollectionPlugins;

    public Collection<BadgeCollectionPlugin> getBadgeCollectionPlugins() {
        return badgeCollectionPlugins;
    }

    public void setBadgeCollectionPlugins(Collection<BadgeCollectionPlugin> badgeCollectionPlugins) {
        this.badgeCollectionPlugins = badgeCollectionPlugins;
    }

    @Override
    public String toString() {
        return "BadgeCollectionPluginResponse{" + "badgeCollectionPlugins=" + badgeCollectionPlugins + '}';
    }
}