package eu.trentorise.game.plugin.point.response;

import eu.trentorise.game.plugin.point.model.PointPlugin;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class PointPluginCollectionResponse {
    
    protected Collection<PointPlugin> pointPlugins;

    public Collection<PointPlugin> getPointPlugins() {
        return pointPlugins;
    }

    public void setPointPlugins(Collection<PointPlugin> pointPlugins) {
        this.pointPlugins = pointPlugins;
    }

    @Override
    public String toString() {
        return "PointPluginCollectionResponse{" + "pointPlugins=" + pointPlugins + '}';
    }
}