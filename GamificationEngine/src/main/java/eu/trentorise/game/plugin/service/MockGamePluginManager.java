package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.controller.container.ICustomizedPluginContainer;
import eu.trentorise.game.plugin.leaderboard.point.model.PointLeaderboardPlugin;
import eu.trentorise.game.plugin.leaderboard.point.model.UpdateRate;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.point.model.Typology;
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
    public GamificationPluginListResponse getCustomizedGamificationPluginList(ICustomizedPluginContainer container) {
        List<GamificationPlugin> list = new ArrayList<>();
        
        //TODO: refactoring of this part changing it with a dynamic mechanism
        //that use persistence. Refactoring by creating different classes (maybe
        //DAO or think about other solutions) able to elaborates, in relation to
        //the specific situation, the list of customized plugins to return
        String name = container.getGamificationPlugin().getName();
        if ("Points".equalsIgnoreCase(name)) {
            list.add(this.createGreenLeavesPointPlugin());
            list.add(this.createHeartsPointPlugin());
            list.add(this.createUsagePointsPointPlugin());
        } else if ("Badge collection".equalsIgnoreCase(name)) {
            list.add(this.createUsageBadgesPlugin());
            list.add(this.createHealthBadgesPlugin());
            list.add(this.createEcologicalBadgesPlugin());
        } else if ("Point Leaderboard".equalsIgnoreCase(name)) {
            list.add(this.createGreenWeeklyLeadearboardPlugin());
            list.add(this.createGreenMonthlyLeadearboardPlugin());
            list.add(this.createUsageCumulativeLeadearboardPlugin());
        }
        
        return this.makeResponse(list);
    }
    
    protected GamificationPlugin createNewPlugin(GamificationPlugin emptyPlugin,
                                                 String name, String version,
                                                 String description) {
        
        if (null == emptyPlugin) {
            emptyPlugin = new GamificationPlugin();
        }
        
        emptyPlugin.setName(name);
        emptyPlugin.setVersion(version);
        emptyPlugin.setDescription(description);
        
        return emptyPlugin;
    }
    
    public GamificationPlugin createPointsPlugin() {
        return this.createNewPlugin(null, "Points", "0.1",
                                    "Points plugin description...");
    }
    
    public GamificationPlugin createBadgeCollectionPlugin() {
        return this.createNewPlugin(null, "Badge collection", "0.1", 
                          "Badge collection plugin description...");
    }
    
    public GamificationPlugin createPointLeadearboardPlugin() {
        return this.createNewPlugin(null, "Point Leaderboard", "0.1", 
                                    "Point Leaderboard plugin description...");
    }
    
    protected GamificationPlugin createNewPointPlugin(String name, 
                                                      String version,
                                                      String description, 
                                                      Typology typology) {
        
        PointPlugin plugin = new PointPlugin();
        plugin = (PointPlugin) this.createNewPlugin(plugin, name, version, 
                                                    description);
        
        plugin.setTypology(typology);
        
        return plugin;
    }
    
    public PointPlugin createGreenLeavesPointPlugin() {
        return (PointPlugin) this.createNewPointPlugin("Green leaves", "0.1", 
                                                       "Description of Green leaves", 
                                                       Typology.SKILL_POINTS);
    }
    
    public PointPlugin createHeartsPointPlugin() {
        return (PointPlugin) this.createNewPointPlugin("Hearts", "0.1", 
                                                       "Description of Hearts", 
                                                       Typology.SKILL_POINTS);
    }
    
    public PointPlugin createUsagePointsPointPlugin() {
        return (PointPlugin) this.createNewPointPlugin("Usage points", "0.1", 
                                                       "Description of Usage points", 
                                                       Typology.SKILL_POINTS);
    }
    
    public GamificationPlugin createUsageBadgesPlugin() {
        return this.createNewPlugin(null, "Usage badges", "0.1", 
                                    "Description of Usage badges");
    }
    
    public GamificationPlugin createHealthBadgesPlugin() {
        return this.createNewPlugin(null, "Health badges", "0.1", 
                                    "Description of Health badges");
    }
    
    public GamificationPlugin createEcologicalBadgesPlugin() {
        return this.createNewPlugin(null, "Ecological badges", "0.1", 
                                    "Description of Ecological badges");
    }
    
    public GamificationPlugin createGreenWeeklyLeadearboardPlugin() {
        return this.createNewPointLeaderboardPlugin("Green weekly leaderboard", "0.1", 
                                                    "Description of Green weekly leaderboard", 
                                                    this.createGreenLeavesPointPlugin(),
                                                    UpdateRate.WEEKLY);
    }
    
    public GamificationPlugin createGreenMonthlyLeadearboardPlugin() {
        return this.createNewPointLeaderboardPlugin("Green monthly leaderboard", "0.1", 
                                                    "Description of Green monthly leaderboard", 
                                                    this.createGreenLeavesPointPlugin(),
                                                    UpdateRate.MONTHLY);
    }
    
    public GamificationPlugin createUsageCumulativeLeadearboardPlugin() {
        return this.createNewPointLeaderboardPlugin("Usage cumulative leaderboard", "0.1", 
                                                    "Description of Usage cumulative leaderboard", 
                                                    this.createUsagePointsPointPlugin(),
                                                    UpdateRate.CUMULATIVE);
    }
    
    public GamificationPlugin createNewPointLeaderboardPlugin(String name, 
                                                              String version,
                                                              String description, 
                                                              PointPlugin dependency,
                                                              UpdateRate updateRate) {
        
        PointLeaderboardPlugin plugin = new PointLeaderboardPlugin();
        plugin = (PointLeaderboardPlugin) this.createNewPlugin(plugin, name, 
                                                               version,
                                                               description);
        
        plugin.setPointPlugin(dependency);
        plugin.setUpdateRate(updateRate);
        
        return plugin;
    }
    
    protected void addNewPlugin(List<GamificationPlugin> list, String name,
                                String version, String description) {
        
        list.add(this.createNewPlugin(null, name, version, description));
    }

    protected GamificationPluginListResponse makeResponse(List<GamificationPlugin> list) {
        GamificationPluginListResponse response = new GamificationPluginListResponse();
        response.setGamificationPlugins(list);
        
        return ((GamificationPluginListResponse) this.buildPositiveResponse(response));
    }
}