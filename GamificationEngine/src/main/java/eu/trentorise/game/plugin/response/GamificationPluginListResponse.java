package eu.trentorise.game.plugin.response;

import eu.trentorise.game.plugin.model.GamificationPlugin;
import eu.trentorise.game.response.*;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class GamificationPluginListResponse extends GameResponse {
    
    protected List<GamificationPlugin> gamificationPlugins;

    public List<GamificationPlugin> getGamificationPlugins() {
        return gamificationPlugins;
    }

    public void setGamificationPlugins(List<GamificationPlugin> gamificationPlugins) {
        this.gamificationPlugins = gamificationPlugins;
    }

    @Override
    public String toString() {
        return "GamificationPluginsListResponse{" + "gamificationPlugins=" + gamificationPlugins + '}';
    }
}