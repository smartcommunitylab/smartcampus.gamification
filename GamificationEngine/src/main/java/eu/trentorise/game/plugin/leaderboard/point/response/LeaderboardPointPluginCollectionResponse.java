package eu.trentorise.game.plugin.leaderboard.point.response;

import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class LeaderboardPointPluginCollectionResponse {
    
    protected Collection<LeaderboardPointPlugin> leaderboardPointPlugins;

    public Collection<LeaderboardPointPlugin> getLeaderboardPointPlugins() {
        return leaderboardPointPlugins;
    }

    public void setLeaderboardPointPlugins(Collection<LeaderboardPointPlugin> leaderboardPointPlugins) {
        this.leaderboardPointPlugins = leaderboardPointPlugins;
    }

    @Override
    public String toString() {
        return "LeaderboardPointPluginCollectionResponse{" + "leaderboardPointPlugins=" + leaderboardPointPlugins + '}';
    }
}