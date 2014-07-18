package eu.trentorise.game.plugin.badgecollection.container;

import eu.trentorise.game.plugin.badgecollection.model.Badge;

/**
 *
 * @author Luca Piras
 */
public interface IBadgeSettingContainer {
    
    public Badge getBadge();

    public void setBadge(Badge badge);
}