package eu.trentorise.game.plugin.point.service;

import eu.trentorise.game.plugin.point.controller.CustomizedPluginContainer;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.plugin.service.IGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.response.MockResponder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockPointPluginManager")
public class MockPointPluginManager extends MockResponder implements IGameCustomizedPluginManager<PointPlugin> {

    @Override
    public CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<PointPlugin> container) {
        //TODO: create new customizedGamificationPlugin, obtain an id, then
        //create a customizedPluginGame using the new 
        //customizedGamificationPlugin with its id and the provided game
        PointPlugin plugin = manager.createGreenLeavesPointPlugin();
        
        CustomizedGamificationPluginResponse response = new CustomizedGamificationPluginResponse();
        response.setCustomizedGamificationPlugin(plugin);
        
        return ((CustomizedGamificationPluginResponse) this.buildPositiveResponse(response));
    }
    
    @Qualifier("mockGamePluginManager")
    @Autowired
    protected MockGamePluginManager manager;
}