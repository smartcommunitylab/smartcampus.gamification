package eu.trentorise.game.plugin.badgecollection.service;

import eu.trentorise.game.plugin.badgecollection.container.IBadgeContainer;
import eu.trentorise.game.plugin.badgecollection.response.BadgeListResponse;

/**
 *
 * @author Luca Piras
 */
public interface IBadgeCollectionPluginManager {
    
    public BadgeListResponse getBadgeList(IBadgeContainer container) throws Exception;
}