package eu.trentorise.game.plugin.comparator;

import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("customizedGamificationPluginKeyComparator")
public class CustomizedGamificationPluginKeyComparator implements Comparator<CustomizedGamificationPlugin>{

    @Override
    public int compare(CustomizedGamificationPlugin o1, CustomizedGamificationPlugin o2) {
        int finalComparison = gamificationPluginComparator.compare(o1, o2);
        if (0 == finalComparison) {
            finalComparison = gamificationPluginComparator.compare(o1.getGamificationPlugin(), o2.getGamificationPlugin());
        }
        
        return finalComparison;
    }
    
    @Qualifier("gamificationPluginKeyComparator")
    @Autowired
    protected Comparator<GamificationPlugin> gamificationPluginComparator;
}