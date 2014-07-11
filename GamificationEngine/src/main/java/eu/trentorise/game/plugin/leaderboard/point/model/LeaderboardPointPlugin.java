package eu.trentorise.game.plugin.leaderboard.point.model;

import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.plugin.point.model.PointPlugin;

/**
 *
 * @author Luca Piras
 */
public class LeaderboardPointPlugin extends CustomizedGamificationPlugin {
    
    protected PointPlugin pointPlugin;
    
    protected UpdateRate updateRate;

    public PointPlugin getPointPlugin() {
        return pointPlugin;
    }

    public void setPointPlugin(PointPlugin pointPlugin) {
        this.pointPlugin = pointPlugin;
    }

    public UpdateRate getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(UpdateRate updateRate) {
        this.updateRate = updateRate;
    }

    @Override
    public String toString() {
        return "PointLeaderboardPlugin{" + "pointPlugin=" + pointPlugin + ", updateRate=" + updateRate + '}';
    }
}