package eu.trentorise.game.plugin.badgecollection.service;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.container.GameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
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


@Service("mockBadgeCollectionPluginManager")
public class MockBadgeCollectionPluginManager implements IRestCrudManager<BadgeCollectionPlugin,
                                                                          Object,
                                                                          BadgeCollectionPlugin>,
                                                         IRestCrudTestManager<BadgeCollectionPlugin,
                                                                              Object,
                                                                              BadgeCollectionPlugin> {
    
    public static MockBadgeCollectionPluginManager createInstance() {
        MockBadgeCollectionPluginManager mock = new MockBadgeCollectionPluginManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockGameCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        mock.comparator = mock.mockGameCustomizedPluginManager.getComparator();
        return mock;
    }
    
    @Override
    public BadgeCollectionPlugin createSingleElement(BadgeCollectionPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return this.createElement(containerWithForeignIds);
    }

    @Override
    public Collection<BadgeCollectionPlugin> readCollection(Object containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella BadgeCollectionPlugin e recupera tutti i
        //customizedPlugins per il plugin indicato
        return this.createElements(containerWithIds);
    }

    @Override
    public BadgeCollectionPlugin readSingleElement(BadgeCollectionPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        BadgeCollectionPlugin returnValue = null;
        
        BadgeCollectionPlugin expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public BadgeCollectionPlugin updateSingleElement(BadgeCollectionPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        BadgeCollectionPlugin returnValue = null;
        
        BadgeCollectionPlugin expectedElement = this.createElement(containerWithForeignIds);
        if (0 == comparator.compare(containerWithForeignIds, expectedElement)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }

    @Override
    public BadgeCollectionPlugin deleteSingleElement(BadgeCollectionPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        BadgeCollectionPlugin returnValue = null;
        
        BadgeCollectionPlugin expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public BadgeCollectionPlugin createElement(BadgeCollectionPlugin containerWithIds) throws Exception {
        return mockPluginManager.createUsageBadgesPlugin();
    }

    @Override
    public Collection createElements(Object containerWithIds) throws Exception {
        Plugin plugin = mockPluginManager.createBadgeCollectionPlugin();
        GameCustomizedPlugin containerContent = mockGameCustomizedPluginManager.createContainerContent(MockGameProfileManager.MOCK_GAME_ID, plugin, null);
        
        IGameCustomizedPluginCollectionContainer container = new GameCustomizedPluginCollectionContainer();
        container.setGameCustomizedPlugin(containerContent);
        
        return mockPluginManager.createCustomizedPlugins(container);
    }

    
    public Comparator<CustomizedPlugin> getComparator() {
        return comparator;
    }
    
    public void setManager(MockPluginManager manager) {
        this.mockPluginManager = manager;
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