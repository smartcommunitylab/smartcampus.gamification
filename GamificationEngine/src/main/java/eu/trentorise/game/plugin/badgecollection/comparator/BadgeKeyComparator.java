package eu.trentorise.game.plugin.badgecollection.comparator;

import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("BadgeKeyComparator")
public class BadgeKeyComparator implements Comparator<Badge> {
    
    @Override
    public int compare(Badge o1, Badge o2) {
        int finalComparison = badgeCollectionPluginComparator.compare(o1.getBadgeCollection(),
                                                                      o2.getBadgeCollection());
        if (0 == finalComparison) {
            finalComparison = o1.getId().compareTo(o2.getId());
        }
        
        return finalComparison;
    }

    
    public void setBadgeCollectionPluginComparator(Comparator<BadgeCollectionPlugin> badgeCollectionPluginComparator) {
        this.badgeCollectionPluginComparator = badgeCollectionPluginComparator;
    }
    
    
    @Qualifier("badgeCollectionKeyComparator")
    @Autowired
    protected Comparator<BadgeCollectionPlugin> badgeCollectionPluginComparator;
}