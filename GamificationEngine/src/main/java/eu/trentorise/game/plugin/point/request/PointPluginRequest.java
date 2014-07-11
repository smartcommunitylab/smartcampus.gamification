package eu.trentorise.game.plugin.point.request;


import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.request.AbstractCustomizedPluginRequest;

/**
 *
 * @author Luca Piras
 */
public class PointPluginRequest extends AbstractCustomizedPluginRequest<PointPlugin> {

    @Override
    public String toString() {
        return "PointPluginRequest{ " + super.toString() + " '}'";
    }
}