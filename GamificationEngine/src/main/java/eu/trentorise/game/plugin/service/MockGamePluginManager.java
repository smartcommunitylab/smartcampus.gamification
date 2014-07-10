package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.container.ICustomizedPluginContainer;
import eu.trentorise.game.plugin.leaderboard.point.model.PointLeaderboardPlugin;
import eu.trentorise.game.plugin.leaderboard.point.model.UpdateRate;
import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.point.model.Typology;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginListResponse;
import eu.trentorise.game.plugin.response.GamificationPluginListResponse;
import eu.trentorise.game.response.MockResponder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGamePluginManager")
public class MockGamePluginManager extends MockResponder implements IGamePluginManager {

    @Override
    public GamificationPluginListResponse getGamificationPluginList() {
        List<GamificationPlugin> list = new ArrayList<>();
        
        list.add(this.createPointsPlugin());
        list.add(this.createBadgeCollectionPlugin());
        list.add(this.createPointLeadearboardPlugin());
        
        return this.makeResponse(list);
    }

    @Override
    public CustomizedGamificationPluginListResponse getCustomizedGamificationPluginList(ICustomizedPluginContainer container) {
        List<CustomizedGamificationPlugin> list = new ArrayList<>();
        
        //TODO: refactoring of this part changing it with a dynamic mechanism
        //that use persistence. Refactoring by creating a DAO that will manage 
        //the getting of customized plugins in relation to the gamification
        //plugin provided (points for instance. The caller know the name of
        //gamification plugins thanks to the getGamificationPluginList service)
        Integer id = container.getGamificationPlugin().getId();
        if (null != id) {
            if (0 == id.compareTo(this.createPointsPlugin().getId())) {
                list.add(this.createGreenLeavesPointPlugin());
                list.add(this.createHeartsPointPlugin());
                list.add(this.createUsagePointsPointPlugin());
            } else if (0 == id.compareTo(this.createBadgeCollectionPlugin().getId())) {
                list.add(this.createUsageBadgesPlugin());
                list.add(this.createHealthBadgesPlugin());
                list.add(this.createEcologicalBadgesPlugin());
            } else if (0 == id.compareTo(this.createPointLeadearboardPlugin().getId())) {
                list.add(this.createGreenWeeklyLeadearboardPlugin());
                list.add(this.createGreenMonthlyLeadearboardPlugin());
                list.add(this.createUsageCumulativeLeadearboardPlugin());
            }
        }
        
        return this.makeCustomizedResponse(list);
    }
    
    protected GamificationPlugin createNewPlugin(GamificationPlugin emptyPlugin,
                                                 Integer id, String name,
                                                 String version,
                                                 String description) {
        
        if (null == emptyPlugin) {
            emptyPlugin = new GamificationPlugin();
        }
        
        emptyPlugin.setId(id);
        emptyPlugin.setName(name);
        emptyPlugin.setVersion(version);
        emptyPlugin.setDescription(description);
        
        return emptyPlugin;
    }
    
    protected CustomizedGamificationPlugin createNewCustomizedPlugin(CustomizedGamificationPlugin emptyPlugin,
                                                                     Integer id, 
                                                                     Integer fatherId,
                                                                     String name,
                                                                     String version,
                                                                     String description) {
        
        if (null == emptyPlugin) {
            emptyPlugin = new CustomizedGamificationPlugin();
        }
        
        this.createNewPlugin(emptyPlugin, fatherId, name, version, description);
        
        emptyPlugin.setId(id);
        
        return emptyPlugin;
    }
    
    public GamificationPlugin createPointsPlugin() {
        return this.createNewPlugin(null, 0, "Points", "0.1",
                                    "Points plugin description...");
    }
    
    public GamificationPlugin createBadgeCollectionPlugin() {
        return this.createNewPlugin(null, 1, "Badge collection", "0.1", 
                          "Badge collection plugin description...");
    }
    
    public GamificationPlugin createPointLeadearboardPlugin() {
        return this.createNewPlugin(null, 2, "Point Leaderboard", "0.1", 
                                    "Point Leaderboard plugin description...");
    }
    
    protected GamificationPlugin createNewPointPlugin(Integer id, 
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
    
    public CustomizedGamificationPlugin createUsageBadgesPlugin() {
        
        CustomizedGamificationPlugin plugin = this.createNewCustomizedPlugin(null, 0, 1, "Usage badges", "0.1", 
                                                                             "Description of Usage badges");
        
        plugin.setGamificationPlugin(this.createBadgeCollectionPlugin());
        
        return plugin;
    }
    
    public CustomizedGamificationPlugin createHealthBadgesPlugin() {
        CustomizedGamificationPlugin plugin = this.createNewCustomizedPlugin(null, 1, 1, "Health badges", "0.1", 
                                                                             "Description of Health badges");
        
        plugin.setGamificationPlugin(this.createBadgeCollectionPlugin());
        
        return plugin;
    }
    
    public CustomizedGamificationPlugin createEcologicalBadgesPlugin() {
        CustomizedGamificationPlugin plugin = this.createNewCustomizedPlugin(null, 2, 1, "Ecological badges", "0.1", 
                                                                             "Description of Ecological badges");
        
        plugin.setGamificationPlugin(this.createBadgeCollectionPlugin());
        
        return plugin;
    }
    
    public CustomizedGamificationPlugin createGreenWeeklyLeadearboardPlugin() {
        return (CustomizedGamificationPlugin) this.createNewPointLeaderboardPlugin(0, 2, "Green weekly leaderboard", "0.1", 
                                                    "Description of Green weekly leaderboard", 
                                                    this.createGreenLeavesPointPlugin(),
                                                    UpdateRate.WEEKLY);
    }
    
    public CustomizedGamificationPlugin createGreenMonthlyLeadearboardPlugin() {
        return (CustomizedGamificationPlugin) this.createNewPointLeaderboardPlugin(1, 2, "Green monthly leaderboard", "0.1", 
                                                    "Description of Green monthly leaderboard", 
                                                    this.createGreenLeavesPointPlugin(),
                                                    UpdateRate.MONTHLY);
    }
    
    public CustomizedGamificationPlugin createUsageCumulativeLeadearboardPlugin() {
        return (CustomizedGamificationPlugin) this.createNewPointLeaderboardPlugin(2, 2, "Usage cumulative leaderboard", "0.1", 
                                                    "Description of Usage cumulative leaderboard", 
                                                    this.createUsagePointsPointPlugin(),
                                                    UpdateRate.CUMULATIVE);
    }
    
    public GamificationPlugin createNewPointLeaderboardPlugin(Integer id,
                                                              Integer fatherId,
                                                              String name, 
                                                              String version,
                                                              String description, 
                                                              PointPlugin dependency,
                                                              UpdateRate updateRate) {
        
        PointLeaderboardPlugin plugin = new PointLeaderboardPlugin();
        plugin = (PointLeaderboardPlugin) this.createNewCustomizedPlugin(plugin,
                                                                         fatherId,
                                                                         id,
                                                                         name, 
                                                                         version,
                                                                         description);
        
        plugin.setGamificationPlugin(this.createPointLeadearboardPlugin());
        plugin.setPointPlugin(dependency);
        plugin.setUpdateRate(updateRate);
        
        return plugin;
    }
    
    protected void addNewPlugin(List<GamificationPlugin> list, Integer id, 
                                String name, String version, 
                                String description) {
        
        list.add(this.createNewPlugin(null, id, name, version, description));
    }

    protected GamificationPluginListResponse makeResponse(List<GamificationPlugin> list) {
        GamificationPluginListResponse response = new GamificationPluginListResponse();
        response.setGamificationPlugins(list);
        
        return ((GamificationPluginListResponse) this.buildPositiveResponse(response));
    }
    
    protected CustomizedGamificationPluginListResponse makeCustomizedResponse(List<CustomizedGamificationPlugin> list) {
        CustomizedGamificationPluginListResponse response = new CustomizedGamificationPluginListResponse();
        response.setCustomizedGamificationPlugins(list);
        
        return ((CustomizedGamificationPluginListResponse) this.buildPositiveResponse(response));
    }
}