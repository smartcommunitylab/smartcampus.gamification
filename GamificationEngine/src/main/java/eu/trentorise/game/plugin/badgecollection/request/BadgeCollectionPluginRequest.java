package eu.trentorise.game.plugin.badgecollection.request;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.request.AbstractCustomizedPluginRequest;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionPluginRequest extends AbstractCustomizedPluginRequest<BadgeCollectionPlugin> {

    @Override
    public String toString() {
        return "BadgeCollectionPluginRequest{ " + super.toString() + " '}'";
    }
}