package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.container.GameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.CustomizedPluginResponse;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockGameCustomizedPluginManager")
public class MockGameCustomizedPluginManager extends MockResponder implements ICustomizedPluginManager<CustomizedPlugin>,
                                                                          IRestCrudManager<CustomizedPlugin, IGameCustomizedPluginCollectionContainer, CustomizedPluginContainer> {

    public static MockGameCustomizedPluginManager createInstance() {
        MockGameCustomizedPluginManager mock = new MockGameCustomizedPluginManager();
        mock.mockPluginManager = new MockPluginManager();
        return mock;
    }
    
    
    @Override
    public CustomizedPlugin createSingleElement(CustomizedPluginContainer containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<CustomizedPlugin> readCollection(IGameCustomizedPluginCollectionContainer containerWithIds) throws Exception {
        return this.mockPluginManager.createCustomizedPlugins(containerWithIds);
    }

    @Override
    public CustomizedPlugin readSingleElement(CustomizedPluginContainer containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CustomizedPlugin updateSingleElement(CustomizedPluginContainer containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CustomizedPlugin deleteSingleElement(CustomizedPluginContainer containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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
    
    public IGameCustomizedPluginCollectionContainer createContainer(Integer gameId, Plugin plugin) {
        Game game = new Game();
        game.setId(gameId);
        
        CustomizedPlugin customizedPlugin = new CustomizedPlugin();
        customizedPlugin.setGamificationPlugin(plugin);
        
        
        GameCustomizedPlugin gcp = new GameCustomizedPlugin();
        
        gcp.setGame(game);
        gcp.setCustomizedPlugin(customizedPlugin);
        
        IGameCustomizedPluginCollectionContainer container = new GameCustomizedPluginCollectionContainer();
        container.setGameCustomizedPlugin(gcp);
        
        return container;
    }
        
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
}