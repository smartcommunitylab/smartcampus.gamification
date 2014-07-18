package eu.trentorise.game.plugin.badgecollection.container;

import eu.trentorise.game.plugin.badgecollection.model.Badge;

/**
 *
 * @author Luca Piras
 */
public class BadgeSettingContainer implements IBadgeSettingContainer {
    
    protected Badge badge;

    @Override
    public Badge getBadge() {
        return badge;
    }

    @Override
    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    @Override
    public String toString() {
        return "BadgeSettingContainer{" + "badge=" + badge + '}';
    }
}