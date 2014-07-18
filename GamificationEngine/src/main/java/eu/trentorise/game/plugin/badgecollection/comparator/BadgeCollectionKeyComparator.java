package eu.trentorise.game.plugin.badgecollection.comparator;

import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("badgeCollectionKeyComparator")
public class BadgeCollectionKeyComparator implements Comparator<BadgeCollectionPlugin> {
    
    @Override
    public int compare(BadgeCollectionPlugin o1, BadgeCollectionPlugin o2) {
        return customizedGamificationPluginComparator.compare(o1, o2);
    }
    
    @Qualifier("customizedGamificationPluginKeyComparator")
    @Autowired
    protected Comparator<CustomizedGamificationPlugin> customizedGamificationPluginComparator;
}