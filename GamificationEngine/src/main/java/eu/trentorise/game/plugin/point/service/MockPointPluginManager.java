package eu.trentorise.game.plugin.point.service;

import eu.trentorise.game.plugin.container.GameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.service.MockGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.Collection;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockPointPluginManager")
public class MockPointPluginManager implements IRestCrudManager<PointPlugin, Object, PointPlugin>,
                                               IRestCrudTestManager<PointPlugin> {

    public static MockPointPluginManager createInstance() {
        MockPointPluginManager mock = new MockPointPluginManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockGameCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        mock.comparator = mock.mockGameCustomizedPluginManager.getComparator();
        return mock;
    }
    
    @Override
    public PointPlugin createSingleElement(PointPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return mockPluginManager.createGreenLeavesPointPlugin();
    }

    @Override
    public Collection<PointPlugin> readCollection(Object containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella PointPlugin e recupera tutti i
        //customizedPlugins per il plugin indicato
        return this.createElements();
    }

    @Override
    public PointPlugin readSingleElement(PointPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        PointPlugin returnValue = null;
        
        PointPlugin expectedElement = this.createElement();
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public PointPlugin updateSingleElement(PointPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        PointPlugin returnValue = null;
        
        PointPlugin expectedElement = this.createElement();
        if (0 == comparator.compare(containerWithForeignIds, expectedElement)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }

    @Override
    public PointPlugin deleteSingleElement(PointPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        PointPlugin returnValue = null;
        
        PointPlugin expectedElement = this.createElement();
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public Collection createElements() {
        Plugin plugin = mockPluginManager.createPointsPlugin();
        GameCustomizedPlugin containerContent = mockGameCustomizedPluginManager.createContainerContent(MockGameProfileManager.MOCK_GAME_ID, plugin, null);
        
        IGameCustomizedPluginCollectionContainer container = new GameCustomizedPluginCollectionContainer();
        container.setGameCustomizedPlugin(containerContent);
        
        return mockPluginManager.createCustomizedPlugins(container);
    }

    @Override
    public PointPlugin createElement() {
        return mockPluginManager.createGreenLeavesPointPlugin();
    }
    
    public Comparator<CustomizedPlugin> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    @Qualifier("mockGameCustomizedPluginManager")
    @Autowired
    protected MockGameCustomizedPluginManager mockGameCustomizedPluginManager;
    
    @Qualifier("customizedPluginKeyComparator")
    @Autowired
    protected Comparator<CustomizedPlugin> comparator;
}