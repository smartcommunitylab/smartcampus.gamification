package eu.trentorise.game.plugin.comparator;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.profile.game.model.Game;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("gameCustomizedPluginKeyComparator")
public class GameCustomizedPluginKeyComparator implements Comparator<GameCustomizedPlugin> {
    
    @Override
    public int compare(GameCustomizedPlugin o1, GameCustomizedPlugin o2) {
        int finalComparison = gameKeyComparator.compare(o1.getGame(), 
                                                        o2.getGame());
        if (0 == finalComparison) {
            finalComparison = customizedPluginKeyComparator.compare(o1.getCustomizedPlugin(), 
                                                                    o2.getCustomizedPlugin());
        }
        
        return finalComparison;
    }

    
    public void setGameKeyComparator(Comparator<Game> gameKeyComparator) {
        this.gameKeyComparator = gameKeyComparator;
    }

    public void setCustomizedPluginKeyComparator(Comparator<CustomizedPlugin> customizedPluginKeyComparator) {
        this.customizedPluginKeyComparator = customizedPluginKeyComparator;
    }
    
    
    @Qualifier("gameKeyComparator")
    @Autowired
    protected Comparator<Game> gameKeyComparator;
    
    @Qualifier("customizedPluginKeyComparator")
    @Autowired
    protected Comparator<CustomizedPlugin> customizedPluginKeyComparator;
}