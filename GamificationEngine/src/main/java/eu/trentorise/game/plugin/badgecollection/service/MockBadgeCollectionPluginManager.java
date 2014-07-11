package eu.trentorise.game.plugin.badgecollection.service;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.plugin.service.IGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.response.MockResponder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockBadgeCollectionPluginManager")
public class MockBadgeCollectionPluginManager extends MockResponder implements IGameCustomizedPluginManager<BadgeCollectionPlugin> {

    @Override
    public CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<BadgeCollectionPlugin> container) {
        //TODO: create new customizedGamificationPlugin, obtain an id, then
        //create a customizedPluginGame using the new 
        //customizedGamificationPlugin with its id and the provided game
        CustomizedGamificationPlugin plugin = manager.createEcologicalBadgesPlugin();
        
        CustomizedGamificationPluginResponse response = new CustomizedGamificationPluginResponse();
        response.setCustomizedGamificationPlugin(plugin);
        
        return ((CustomizedGamificationPluginResponse) this.buildPositiveResponse(response));
    }
    
    @Qualifier("mockGamePluginManager")
    @Autowired
    protected MockGamePluginManager manager;
}