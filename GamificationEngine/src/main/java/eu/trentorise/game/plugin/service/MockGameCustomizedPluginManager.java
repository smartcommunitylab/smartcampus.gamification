package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.plugin.point.controller.CustomizedPluginContainer;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.response.MockResponder;
import org.springframework.stereotype.Service;


//@Service("mockGameCustomizedPluginManager")
public class MockGameCustomizedPluginManager extends MockResponder implements IGameCustomizedPluginManager<CustomizedGamificationPlugin> {

    @Override
    public CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<CustomizedGamificationPlugin> container) {
        //TODO: create new customizedGamificationPlugin, obtain an id, then
        //create a customizedPluginGame using the new 
        //customizedGamificationPlugin with its id and the provided game
                
        CustomizedGamificationPlugin plugin = container.getPlugin();
        plugin.setId(5);
        
        CustomizedGamificationPluginResponse response = new CustomizedGamificationPluginResponse();
        response.setCustomizedGamificationPlugin(plugin);
        
        return ((CustomizedGamificationPluginResponse) this.buildPositiveResponse(response));
    }
}