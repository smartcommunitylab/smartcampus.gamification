package eu.trentorise.game.plugin.comparator;

import eu.trentorise.game.plugin.model.Plugin;
import java.util.Comparator;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("pluginKeyComparator")
public class PluginKeyComparator implements Comparator<Plugin>{

    @Override
    public int compare(Plugin o1, Plugin o2) {
        return o1.getId().compareTo(o2.getId());
    }
}