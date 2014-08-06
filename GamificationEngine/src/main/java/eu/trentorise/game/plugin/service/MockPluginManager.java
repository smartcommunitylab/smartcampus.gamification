package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.comparator.PluginKeyComparator;
import eu.trentorise.game.plugin.container.ICustomizedPluginActivationDeactivationContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IPluginContainer;
import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;
import eu.trentorise.game.plugin.leaderboard.point.model.UpdateRate;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.point.model.Typology;
import eu.trentorise.game.plugin.response.CustomizedPluginCollectionResponse;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockPluginManager")
public class MockPluginManager extends MockResponder implements IGameCustomizedPluginManager,
                                                                IRestCrudManager<Plugin, Object, IPluginContainer> {

    public static MockPluginManager createInstance() {
        MockPluginManager mock = new MockPluginManager();
        mock.comparator = new PluginKeyComparator();
        return mock;
    }
    
    
    @Override
    public Plugin createSingleElement(IPluginContainer containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Plugin> readCollection(Object containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        return this.createElements();
    }

    @Override
    public Plugin readSingleElement(IPluginContainer containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        Plugin returnValue = null;
        
        Plugin expectedElement = this.createPointsPlugin();
        if (0 == comparator.compare(containerWithIds.getGamificationPlugin(), expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public Plugin updateSingleElement(IPluginContainer containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Plugin deleteSingleElement(IPluginContainer containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public Collection<Plugin> createElements() {
        Collection<Plugin> list = new ArrayList<>();
        
        list.add(this.createPointsPlugin());
        list.add(this.createBadgeCollectionPlugin());
        list.add(this.createLeadearboardPointPlugin());
        
        return list;
    }
    
    public Collection<CustomizedPlugin> createCustomizedPlugins(IGameCustomizedPluginCollectionContainer container) {
        List<CustomizedPlugin> list = new ArrayList<>();
        //TODO: vai nella tabella gameCustomizedPlugin e recupera tutti i
        //customizedPlugins per il game indicato ed il plugin indicato
        //l'id del customizedPlugin non deve essere ovviamente settato
        
        //TODO: refactoring of this part changing it with a dynamic mechanism
        //that use persistence. Refactoring by creating a DAO that will manage 
        //the getting of customized plugins in relation to the gamification
        //plugin provided (points for instance. The caller know the name of
        //gamification plugins thanks to the getGamificationPluginList service)
        Integer id = container.getGameCustomizedPlugin().getCustomizedPlugin().getGamificationPlugin().getId();
        if (null != id) {
            if (0 == id.compareTo(this.createPointsPlugin().getId())) {
                list.add(this.createGreenLeavesPointPlugin());
                list.add(this.createHeartsPointPlugin());
                list.add(this.createUsagePointsPointPlugin());
            } else if (0 == id.compareTo(this.createBadgeCollectionPlugin().getId())) {
                list.add(this.createUsageBadgesPlugin());
                list.add(this.createHealthBadgesPlugin());
                list.add(this.createEcologicalBadgesPlugin());
            } else if (0 == id.compareTo(this.createLeadearboardPointPlugin().getId())) {
                list.add(this.createGreenWeeklyLeadearboardPlugin());
                list.add(this.createGreenMonthlyLeadearboardPlugin());
                list.add(this.createUsageCumulativeLeadearboardPlugin());
            }
        }
        
        return list;
    }
    
    @Override
    public GameResponse activateDeactivateCustomizedGamificationPlugin(ICustomizedPluginActivationDeactivationContainer container) {
        return this.buildPositiveResponse(new GameResponse());
    }
    
    protected Plugin createNewPlugin(Plugin emptyPlugin,
                                                 Integer id, String name,
                                                 String version,
                                                 String description) {
        
        if (null == emptyPlugin) {
            emptyPlugin = new Plugin();
        }
        
        emptyPlugin.setId(id);
        emptyPlugin.setName(name);
        emptyPlugin.setVersion(version);
        emptyPlugin.setDescription(description);
        
        return emptyPlugin;
    }
    
    protected CustomizedPlugin createNewCustomizedPlugin(CustomizedPlugin emptyPlugin,
                                                                     Integer id, 
                                                                     Integer fatherId,
                                                                     String name,
                                                                     String version,
                                                                     String description) {
        
        if (null == emptyPlugin) {
            emptyPlugin = new CustomizedPlugin();
        }
        
        this.createNewPlugin(emptyPlugin, fatherId, name, version, description);
        
        emptyPlugin.setId(id);
        
        return emptyPlugin;
    }
    
    public Plugin createPointsPlugin() {
        return this.createNewPlugin(null, 0, "Points", "0.1",
                                    "Points plugin description...");
    }
    
    public Plugin createBadgeCollectionPlugin() {
        return this.createNewPlugin(null, 1, "Badge collection", "0.1", 
                          "Badge collection plugin description...");
    }
    
    public Plugin createLeadearboardPointPlugin() {
        return this.createNewPlugin(null, 2, "Point Leaderboard", "0.1", 
                                    "Point Leaderboard plugin description...");
    }
    
    protected Plugin createNewPointPlugin(Integer id, 
                                                      Integer fatherId,
                                                      String name, 
                                                      String version,
                                                      String description, 
                                                      Typology typology) {
        
        PointPlugin plugin = new PointPlugin();
        plugin = (PointPlugin) this.createNewCustomizedPlugin(plugin, id, 
                                                              fatherId, name,
                                                              version,
                                                              description);
        
        plugin.setGamificationPlugin(this.createPointsPlugin());
        plugin.setTypology(typology);
        
        return plugin;
    }
    
    public PointPlugin createGreenLeavesPointPlugin() {
        return (PointPlugin) this.createNewPointPlugin(0, 0, "Green leaves", "0.1", 
                                                       "Description of Green leaves", 
                                                       Typology.SKILL_POINTS);
    }
    
    public PointPlugin createHeartsPointPlugin() {
        return (PointPlugin) this.createNewPointPlugin(1, 0, "Hearts", "0.1", 
                                                       "Description of Hearts", 
                                                       Typology.SKILL_POINTS);
    }
    
    public PointPlugin createUsagePointsPointPlugin() {
        return (PointPlugin) this.createNewPointPlugin(2, 0, "Usage points", "0.1", 
                                                       "Description of Usage points", 
                                                       Typology.SKILL_POINTS);
    }
    
    public BadgeCollectionPlugin createNewBadgeCollectionPlugin(Integer id,
                                                                Integer fatherId,
                                                                String name, 
                                                                String version,
                                                                String description) {
        
        BadgeCollectionPlugin plugin = new BadgeCollectionPlugin();
        plugin = (BadgeCollectionPlugin) this.createNewCustomizedPlugin(plugin,
                                                                        id,
                                                                        fatherId,
                                                                        name, 
                                                                        version,
                                                                        description);
        
        plugin.setGamificationPlugin(this.createBadgeCollectionPlugin());
        
        return plugin;
    }
    
    public BadgeCollectionPlugin createUsageBadgesPlugin() {
        return this.createNewBadgeCollectionPlugin(0, 1, "Usage badges", "0.1", 
                                                   "Description of Usage badges");
    }
    
    public BadgeCollectionPlugin createHealthBadgesPlugin() {
        return this.createNewBadgeCollectionPlugin(1, 1, "Health badges", "0.1", 
                                                   "Description of Health badges");
    }
    
    public BadgeCollectionPlugin createEcologicalBadgesPlugin() {
        return this.createNewBadgeCollectionPlugin(2, 1, 
                                                   "Ecological badges", "0.1",
                                                   "Description of Ecological badges");
    }
    
    public LeaderboardPointPlugin createGreenWeeklyLeadearboardPlugin() {
        return this.createNewPointLeaderboardPlugin(0, 2, "Green weekly leaderboard", "0.1", 
                                                    "Description of Green weekly leaderboard", 
                                                    this.createGreenLeavesPointPlugin(),
                                                    UpdateRate.WEEKLY);
    }
    
    public LeaderboardPointPlugin createGreenMonthlyLeadearboardPlugin() {
        return this.createNewPointLeaderboardPlugin(1, 2, "Green monthly leaderboard", "0.1", 
                                                    "Description of Green monthly leaderboard", 
                                                    this.createGreenLeavesPointPlugin(),
                                                    UpdateRate.MONTHLY);
    }
    
    public LeaderboardPointPlugin createUsageCumulativeLeadearboardPlugin() {
        return this.createNewPointLeaderboardPlugin(2, 2, "Usage cumulative leaderboard", "0.1", 
                                                    "Description of Usage cumulative leaderboard", 
                                                    this.createUsagePointsPointPlugin(),
                                                    UpdateRate.CUMULATIVE);
    }
    
    public LeaderboardPointPlugin createNewPointLeaderboardPlugin(Integer id,
                                                                  Integer fatherId,
                                                                  String name, 
                                                                  String version,
                                                                  String description, 
                                                                  PointPlugin dependency,
                                                                  UpdateRate updateRate) {
        
        LeaderboardPointPlugin plugin = new LeaderboardPointPlugin();
        plugin = (LeaderboardPointPlugin) this.createNewCustomizedPlugin(plugin,
                                                                         fatherId,
                                                                         id,
                                                                         name, 
                                                                         version,
                                                                         description);
        
        plugin.setGamificationPlugin(this.createLeadearboardPointPlugin());
        plugin.setPointPlugin(dependency);
        plugin.setUpdateRate(updateRate);
        
        return plugin;
    }
    
    protected void addNewPlugin(List<Plugin> list, Integer id, 
                                String name, String version, 
                                String description) {
        
        list.add(this.createNewPlugin(null, id, name, version, description));
    }
    
    @Qualifier("pluginKeyComparator")
    @Autowired
    protected Comparator<Plugin> comparator;
}