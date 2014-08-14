package eu.trentorise.game.ruleengine.comparator;

import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.ruleengine.model.GameRule;
import eu.trentorise.game.ruleengine.model.Rule;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("gameRuleKeyComparator")
public class GameRuleKeyComparator implements Comparator<GameRule> {
    
    @Override
    public int compare(GameRule o1, GameRule o2) {
        int finalComparison = gameKeyComparator.compare(o1.getGame(), 
                                                        o2.getGame());
        if (0 == finalComparison) {
            finalComparison = ruleKeyComparator.compare(o1.getRule(), o2.getRule());
        }
        
        return finalComparison;
    }

    
    public void setGameKeyComparator(Comparator<Game> gameKeyComparator) {
        this.gameKeyComparator = gameKeyComparator;
    }

    public void setRuleKeyComparator(Comparator<Rule> ruleKeyComparator) {
        this.ruleKeyComparator = ruleKeyComparator;
    }
    
    
    @Qualifier("gameKeyComparator")
    @Autowired
    protected Comparator<Game> gameKeyComparator;
    
    @Qualifier("ruleKeyComparator")
    @Autowired
    protected Comparator<Rule> ruleKeyComparator;
}