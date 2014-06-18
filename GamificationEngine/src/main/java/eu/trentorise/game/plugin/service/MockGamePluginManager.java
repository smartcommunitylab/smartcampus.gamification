package eu.trentorise.game.plugin.service;

import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.plugin.response.GamificationPluginsListResponse;
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
    public GamificationPluginsListResponse getGamificationPluginList() {
        List<GamificationPlugin> list = new ArrayList<>();
        
        this.addNewPlugin(list, "Points", "0.1", 
                          "Points plugin description...");
        
        this.addNewPlugin(list, "Badge collection", "0.1", 
                          "Badge collection plugin description...");
        
        this.addNewPlugin(list, "Point Leaderboard", "0.1", 
                          "Point Leaderboard plugin description...");
        
        GamificationPluginsListResponse response = new GamificationPluginsListResponse();
        response.setGamificationPlugins(list);
        
        return ((GamificationPluginsListResponse) this.buildPositiveResponse(response));
    }

    protected void addNewPlugin(List<GamificationPlugin> list, String name,
                                String version, String description) {
        
        GamificationPlugin plugin = new GamificationPlugin();
        
        plugin.setName(name);
        plugin.setVersion(version);
        plugin.setDescription(description);
        
        list.add(plugin);
    }
}