package eu.trentorise.game.plugin.badgecollection.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.container.BadgeContainer;
import eu.trentorise.game.plugin.badgecollection.container.IBadgeContainer;
import eu.trentorise.game.plugin.badgecollection.request.BadgeCollectionPluginRequest;
import eu.trentorise.game.plugin.badgecollection.request.BadgeRequest;
import eu.trentorise.game.plugin.badgecollection.response.BadgeListResponse;
import eu.trentorise.game.plugin.badgecollection.service.IBadgeCollectionPluginManager;
import eu.trentorise.game.plugin.controller.AbstractCustomizedPluginController;
import eu.trentorise.game.plugin.request.AbstractCustomizedPluginRequest;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.plugin.service.IGameCustomizedPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("badgeCollectionPluginController")
@RequestMapping(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_PATH)
public class BadgeCollectionPluginController extends AbstractCustomizedPluginController {
    
    @RequestMapping(method = RequestMethod.POST, value = "/setCustomizedGamificationPlugin" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(@RequestBody BadgeCollectionPluginRequest request) {
        return super.setCustomizedGamificationPlugin((AbstractCustomizedPluginRequest) request);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getBadgeList" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody BadgeListResponse getBadgeList(@RequestBody BadgeRequest request) throws Exception {
        IBadgeContainer container = new BadgeContainer();
        container.setBadgeCollection(request.getBadgeCollection());
        
        return badgeCollectionPluginManager.getBadgeList(container);
    }
    
    
    @Qualifier("mockBadgeCollectionPluginManager")
    @Autowired
    public void setManager(IGameCustomizedPluginManager manager) {
        this.manager = manager;
    }
            
    @Qualifier("mockBadgeCollectionPluginManager")
    @Autowired
    protected IBadgeCollectionPluginManager badgeCollectionPluginManager;
}