package eu.trentorise.game.plugin.point.response;

import eu.trentorise.game.plugin.point.model.PointPlugin;

/**
 *
 * @author Luca Piras
 */
public class PointPluginResponse {
    
    protected PointPlugin pointPlugin;

    public PointPlugin getPointPlugin() {
        return pointPlugin;
    }

    public void setPointPlugin(PointPlugin pointPlugin) {
        this.pointPlugin = pointPlugin;
    }

    @Override
    public String toString() {
        return "PointPluginResponse{" + "pointPlugin=" + pointPlugin + '}';
    }
}