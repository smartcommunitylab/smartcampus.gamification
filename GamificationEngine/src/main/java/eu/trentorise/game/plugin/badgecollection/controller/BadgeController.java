package eu.trentorise.game.plugin.badgecollection.controller;

import eu.trentorise.game.plugin.badgecollection.service.IBadgeManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.container.BadgeSettingContainer;
import eu.trentorise.game.plugin.badgecollection.container.IBadgeSettingContainer;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.response.BadgeResponse;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Luca Piras
 */
@Controller("badgeCollectionPluginBadgeController")
@RequestMapping(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH)
public class BadgeController {
    
    @RequestMapping(method = RequestMethod.POST, value = "/setBadge" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody BadgeResponse setBadge(@RequestParam(value = IGameConstants.PARAM_BADGECOLLECTION_ID, required = true) Integer badgeCollectionId,
                                                @RequestParam(value = IGameConstants.PARAM_BADGECOLLECTION_PLUGIN_ID, required = true) Integer badgeCollectionPluginId,
                                                @RequestParam(value = IGameConstants.PARAM_BADGECOLLECTION_BADGE_FILE, required = false) MultipartFile badgeFile) throws Exception {
        
        IBadgeSettingContainer container = new BadgeSettingContainer();
        
        Badge badge = new Badge();
        
        BadgeCollectionPlugin badgeCollection = new BadgeCollectionPlugin();
        badgeCollection.setId(badgeCollectionId);
        
        GamificationPlugin gamificationPlugin = new GamificationPlugin();
        gamificationPlugin.setId(badgeCollectionPluginId);
        
        badgeCollection.setGamificationPlugin(gamificationPlugin);
        
        badge.setBadgeCollection(badgeCollection);
        
        container.setBadge(badge);
        
        return manager.setBadge(container);
    }
            
    //TODO: in the final implementation the following manager will be a 
    //separated class (at the moment it is in mockBadgeCollectionPluginManager)
    @Qualifier("mockBadgeCollectionPluginManager")
    @Autowired
    protected IBadgeManager manager;
}