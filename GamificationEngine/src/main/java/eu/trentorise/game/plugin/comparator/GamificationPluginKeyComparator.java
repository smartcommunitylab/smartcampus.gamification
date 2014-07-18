package eu.trentorise.game.plugin.comparator;

import eu.trentorise.game.plugin.model.GamificationPlugin;
import java.util.Comparator;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("gamificationPluginKeyComparator")
public class GamificationPluginKeyComparator implements Comparator<GamificationPlugin>{

    @Override
    public int compare(GamificationPlugin o1, GamificationPlugin o2) {
        return o1.getId().compareTo(o2.getId());
    }
}