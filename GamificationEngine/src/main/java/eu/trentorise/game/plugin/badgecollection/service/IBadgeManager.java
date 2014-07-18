package eu.trentorise.game.plugin.badgecollection.service;

import eu.trentorise.game.plugin.badgecollection.container.IBadgeSettingContainer;
import eu.trentorise.game.plugin.badgecollection.response.BadgeResponse;

/**
 *
 * @author Luca Piras
 */
public interface IBadgeManager {
    
    public BadgeResponse setBadge(IBadgeSettingContainer container) throws Exception;
}