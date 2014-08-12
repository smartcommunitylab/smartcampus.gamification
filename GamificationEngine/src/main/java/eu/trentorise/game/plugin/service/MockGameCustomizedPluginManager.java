package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.comparator.CustomizedPluginKeyComparator;
import eu.trentorise.game.plugin.comparator.GameCustomizedPluginKeyComparator;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockGameCustomizedPluginManager")
public class MockGameCustomizedPluginManager implements IRestCrudManager<GameCustomizedPlugin, 
                                                                         GameCustomizedPlugin, 
                                                                         GameCustomizedPlugin>,
                                                        IRestCrudTestManager<GameCustomizedPlugin, 
                                                                             GameCustomizedPlugin, 
                                                                             GameCustomizedPlugin> {

    public static MockGameCustomizedPluginManager createInstance() {
        MockGameCustomizedPluginManager mock = new MockGameCustomizedPluginManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockGameProfileManager = MockGameProfileManager.createInstance();
        
        mock.customizedPluginComparator = new CustomizedPluginKeyComparator();
        ((CustomizedPluginKeyComparator) mock.customizedPluginComparator).setPluginComparator(mock.mockPluginManager.getComparator());
        mock.comparator = new GameCustomizedPluginKeyComparator();
        ((GameCustomizedPluginKeyComparator) mock.comparator).setGameKeyComparator(mock.mockGameProfileManager.getComparator());
        ((GameCustomizedPluginKeyComparator) mock.comparator).setCustomizedPluginKeyComparator(mock.customizedPluginComparator);
        
        return mock;
    }
    
    
    @Override
    public GameCustomizedPlugin createSingleElement(GameCustomizedPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return this.createElement(containerWithForeignIds);
    }

    @Override
    public Collection<GameCustomizedPlugin> readCollection(GameCustomizedPlugin containerWithIds) throws Exception {
        //TODO: vai nella tabella gameCustomizedPlugin e recupera tutti i
        //customizedPlugins per il game indicato ed il plugin indicato
        //l'id del customizedPlugin non deve essere ovviamente settato
        return this.createElements(containerWithIds);
    }

    @Override
    public GameCustomizedPlugin readSingleElement(GameCustomizedPlugin containerWithIds) throws Exception {
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
        GameCustomizedPlugin returnValue = null;
        
        GameCustomizedPlugin expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public GameCustomizedPlugin updateSingleElement(GameCustomizedPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        GameCustomizedPlugin returnValue = null;
        
        GameCustomizedPlugin expectedElement = this.createElement(containerWithForeignIds);
        if (0 == comparator.compare(containerWithForeignIds, expectedElement)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }

    @Override
    public GameCustomizedPlugin deleteSingleElement(GameCustomizedPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        GameCustomizedPlugin returnValue = null;
        
        GameCustomizedPlugin expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public GameCustomizedPlugin createElement(GameCustomizedPlugin containerWithIds) throws Exception {
        Game game = mockGameProfileManager.createElement();
        PointPlugin pointPlugin = mockPluginManager.createGreenLeavesPointPlugin();
        
        GameCustomizedPlugin element = new GameCustomizedPlugin();
        element.setGame(game);
        element.setCustomizedPlugin(pointPlugin);
        element.setActivated(Boolean.TRUE);
        
        return element;
    }

    @Override
    public Collection<GameCustomizedPlugin> createElements(GameCustomizedPlugin containerWithIds) throws Exception {
        Collection<CustomizedPlugin> customizedPlugins = this.mockPluginManager.createCustomizedPlugins(containerWithIds);
        
        Collection<GameCustomizedPlugin> gameCustomizedPlugins = new ArrayList<>();
        for (CustomizedPlugin current : customizedPlugins) {
            GameCustomizedPlugin gcp = new GameCustomizedPlugin();
            gcp.setGame(mockGameProfileManager.createElement());
            gcp.setCustomizedPlugin(current);
            gcp.setActivated(Boolean.TRUE);
            gameCustomizedPlugins.add(gcp);
        }
        
        return gameCustomizedPlugins;
    }
    
    
    public GameCustomizedPlugin createContainer(Integer gameId, Plugin plugin, Integer cusPlugId) {
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

    
    public Comparator<CustomizedPlugin> getCustomizedPluginComparator() {
        return customizedPluginComparator;
    }
    
    public Comparator<GameCustomizedPlugin> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    @Qualifier("mockGameProfileManager")
    @Autowired
    protected MockGameProfileManager mockGameProfileManager;
    
    
    @Qualifier("customizedPluginKeyComparator")
    @Autowired
    protected Comparator<CustomizedPlugin> customizedPluginComparator;
    
    @Qualifier("gameCustomizedPluginKeyComparator")
    @Autowired
    protected Comparator<GameCustomizedPlugin> comparator;
}