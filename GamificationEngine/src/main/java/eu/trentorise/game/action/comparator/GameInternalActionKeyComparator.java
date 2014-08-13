package eu.trentorise.game.action.comparator;

import eu.trentorise.game.action.model.GameInternalAction;
import eu.trentorise.game.action.model.InternalAction;
import eu.trentorise.game.profile.game.model.Game;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("gameInternalActionKeyComparator")
public class GameInternalActionKeyComparator implements Comparator<GameInternalAction> {

    @Override
    public int compare(GameInternalAction o1, GameInternalAction o2) {
        int finalComparison = gameKeyComparator.compare(o1.getGame(), o2.getGame());
        if (0 == finalComparison) {
            finalComparison = internalActionKeyComparator.compare(o1.getInternalAction(), o2.getInternalAction());
        }
        
        return finalComparison;
    }
    

    public void setGameKeyComparator(Comparator<Game> gameKeyComparator) {
        this.gameKeyComparator = gameKeyComparator;
    }

    public void setInternalActionKeyComparator(Comparator<InternalAction> internalActionKeyComparator) {
        this.internalActionKeyComparator = internalActionKeyComparator;
    }
    
    
    @Qualifier("gameKeyComparator")
    @Autowired
    protected Comparator<Game> gameKeyComparator;
    
    @Qualifier("internalActionKeyComparator")
    @Autowired
    protected Comparator<InternalAction> internalActionKeyComparator;
}