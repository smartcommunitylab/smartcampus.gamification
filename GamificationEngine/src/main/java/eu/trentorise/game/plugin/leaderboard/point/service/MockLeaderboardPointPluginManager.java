package eu.trentorise.game.plugin.leaderboard.point.service;

import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;
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


@Service("mockLeaderboardPointPluginManager")
public class MockLeaderboardPointPluginManager implements IRestCrudManager<LeaderboardPointPlugin, Object, LeaderboardPointPlugin>,
                                                          IRestCrudTestManager<LeaderboardPointPlugin, Object, LeaderboardPointPlugin> {
    
    public static MockLeaderboardPointPluginManager createInstance() {
        MockLeaderboardPointPluginManager mock = new MockLeaderboardPointPluginManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockGameCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        mock.comparator = mock.mockGameCustomizedPluginManager.getCustomizedPluginComparator();
        return mock;
    }
    
    @Override
    public LeaderboardPointPlugin createSingleElement(LeaderboardPointPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return this.createElement(containerWithForeignIds);
    }
    
    @Override
    public Collection<LeaderboardPointPlugin> readCollection(Object containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella LeaderboardPointPlugin e recupera tutti i
        //customizedPlugins per il plugin indicato
        return this.createElements(containerWithIds);
    }

    @Override
    public LeaderboardPointPlugin readSingleElement(LeaderboardPointPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        LeaderboardPointPlugin returnValue = null;
        
        LeaderboardPointPlugin expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public LeaderboardPointPlugin updateSingleElement(LeaderboardPointPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        LeaderboardPointPlugin returnValue = null;
        
        LeaderboardPointPlugin expectedElement = this.createElement(containerWithForeignIds);
        if (0 == comparator.compare(containerWithForeignIds, expectedElement)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }

    @Override
    public LeaderboardPointPlugin deleteSingleElement(LeaderboardPointPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        LeaderboardPointPlugin returnValue = null;
        
        LeaderboardPointPlugin expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public Collection createElements(Object containerWithIds) throws Exception {
        Plugin plugin = mockPluginManager.createLeadearboardPointPlugin();
        GameCustomizedPlugin container = mockGameCustomizedPluginManager.createContainer(MockGameProfileManager.MOCK_GAME_ID, plugin, null);
        
        return mockPluginManager.createCustomizedPlugins(container);
    }

    @Override
    public LeaderboardPointPlugin createElement(LeaderboardPointPlugin containerWithIds) throws Exception {
        return mockPluginManager.createGreenWeeklyLeadearboardPlugin();
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