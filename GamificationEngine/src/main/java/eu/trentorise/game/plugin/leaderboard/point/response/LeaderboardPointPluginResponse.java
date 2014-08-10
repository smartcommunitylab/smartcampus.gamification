package eu.trentorise.game.plugin.leaderboard.point.response;

import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;

/**
 *
 * @author Luca Piras
 */
public class LeaderboardPointPluginResponse {
    
    protected LeaderboardPointPlugin leaderboardPointPlugin;

    public LeaderboardPointPlugin getLeaderboardPointPlugin() {
        return leaderboardPointPlugin;
    }

    public void setLeaderboardPointPlugin(LeaderboardPointPlugin leaderboardPointPlugin) {
        this.leaderboardPointPlugin = leaderboardPointPlugin;
    }

    @Override
    public String toString() {
        return "LeaderboardPointPluginResponse{" + "leaderboardPointPlugin=" + leaderboardPointPlugin + '}';
    }
}