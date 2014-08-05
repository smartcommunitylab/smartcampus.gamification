package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.response.CustomizedPluginResponse;
import eu.trentorise.game.response.MockResponder;
import org.springframework.stereotype.Service;


//@Service("mockGameCustomizedPluginManager")
public class MockCustomizedPluginManager extends MockResponder implements ICustomizedPluginManager<CustomizedPlugin> {

    @Override
    public CustomizedPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<CustomizedPlugin> container) {
        //TODO: create new customizedGamificationPlugin, obtain an id, then
        //create a customizedPluginGame using the new 
        //customizedGamificationPlugin with its id and the provided game
                
        CustomizedPlugin plugin = container.getPlugin();
        plugin.setId(5);
        
        CustomizedPluginResponse response = new CustomizedPluginResponse();
        response.setCustomizedPlugin(plugin);
        
        return ((CustomizedPluginResponse) this.buildPositiveResponse(response));
    }
}