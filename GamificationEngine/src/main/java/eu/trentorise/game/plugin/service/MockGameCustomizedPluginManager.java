package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.comparator.CustomizedPluginKeyComparator;
import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.SettingCustomizedPluginResponse;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import java.util.Collection;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockGameCustomizedPluginManager")
public class MockGameCustomizedPluginManager extends MockResponder implements ICustomizedPluginManager<CustomizedPlugin>,
                                                        IRestCrudManager<CustomizedPlugin, 
                                                                         IGameCustomizedPluginCollectionContainer, 
                                                                         IGameCustomizedPluginContainer> {

    public static MockGameCustomizedPluginManager createInstance() {
        MockGameCustomizedPluginManager mock = new MockGameCustomizedPluginManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.comparator = new CustomizedPluginKeyComparator();
        ((CustomizedPluginKeyComparator) mock.comparator).setPluginComparator(mock.mockPluginManager.getComparator());
        return mock;
    }
    
    
    @Override
    public CustomizedPlugin createSingleElement(IGameCustomizedPluginContainer containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<CustomizedPlugin> readCollection(IGameCustomizedPluginCollectionContainer containerWithIds) throws Exception {
        //TODO: vai nella tabella gameCustomizedPlugin e recupera tutti i
        //customizedPlugins per il game indicato ed il plugin indicato
        //l'id del customizedPlugin non deve essere ovviamente settato
        return this.mockPluginManager.createCustomizedPlugins(containerWithIds);
    }

    @Override
    public CustomizedPlugin readSingleElement(IGameCustomizedPluginContainer containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella gameCustomizedPlugin e recupera tutti i
        //customizedPlugins per il game indicato, il plugin indicato e
        //l'id del customizedPlugin indicato, fai il join inoltre con la
        //tabella CustomizedPlugin per ottenere le info di dettaglio
        //TODO: IMPORTANTE: anche il gameId ha importanza qui, deve matchare
        //durante la query, non andare direttamente ad interrogare la tabella
        //CustomizedPlugin bypassando il gameId, i customizedPlugins da rendere
        //sono quelli del gioco, e del customizedPlugin (key id piu' plugin id
        //del plugin interno al CustomizedPlugin) indicati
        CustomizedPlugin returnValue = null;
        
        CustomizedPlugin expectedElement = this.mockPluginManager.createGreenLeavesPointPlugin();
        if (0 == comparator.compare(containerWithIds.getGameCustomizedPlugin().getCustomizedPlugin(),
                                    expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public CustomizedPlugin updateSingleElement(IGameCustomizedPluginContainer containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CustomizedPlugin deleteSingleElement(IGameCustomizedPluginContainer containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public SettingCustomizedPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<CustomizedPlugin> container) {
        //TODO: create new customizedGamificationPlugin, obtain an id, then
        //create a customizedPluginGame using the new 
        //customizedGamificationPlugin with its id and the provided game
                
        CustomizedPlugin plugin = container.getPlugin();
        plugin.setId(5);
        
        SettingCustomizedPluginResponse response = new SettingCustomizedPluginResponse();
        response.setCustomizedPlugin(plugin);
        
        return ((SettingCustomizedPluginResponse) this.buildPositiveResponse(response));
    }
    
    public GameCustomizedPlugin createContainerContent(Integer gameId, Plugin plugin, Integer cusPlugId) {
        Game game = new Game();
        game.setId(gameId);
        
        CustomizedPlugin customizedPlugin = new CustomizedPlugin();
        customizedPlugin.setId(cusPlugId);
        customizedPlugin.setPlugin(plugin);
        
        
        GameCustomizedPlugin gcp = new GameCustomizedPlugin();
        
        gcp.setGame(game);
        gcp.setCustomizedPlugin(customizedPlugin);
        
        return gcp;
    }

    
    public Comparator<CustomizedPlugin> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    @Qualifier("customizedPluginKeyComparator")
    @Autowired
    protected Comparator<CustomizedPlugin> comparator;
}