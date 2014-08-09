package eu.trentorise.game.plugin.comparator;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("customizedPluginKeyComparator")
public class CustomizedPluginKeyComparator implements Comparator<CustomizedPlugin>{

    @Override
    public int compare(CustomizedPlugin o1, CustomizedPlugin o2) {
        int finalComparison = pluginComparator.compare(o1, o2);
        if (0 == finalComparison) {
            finalComparison = pluginComparator.compare(o1.getPlugin(), o2.getPlugin());
        }
        
        return finalComparison;
    }

    public void setPluginComparator(Comparator<Plugin> pluginComparator) {
        this.pluginComparator = pluginComparator;
    }
    
    @Qualifier("pluginKeyComparator")
    @Autowired
    protected Comparator<Plugin> pluginComparator;
}