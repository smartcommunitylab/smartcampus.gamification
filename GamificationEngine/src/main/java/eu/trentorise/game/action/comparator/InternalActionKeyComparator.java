package eu.trentorise.game.action.comparator;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.InternalAction;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("internalActionKeyComparator")
public class InternalActionKeyComparator implements Comparator<InternalAction>{

    @Override
    public int compare(InternalAction o1, InternalAction o2) {
        int finalComparison = actionKeyComparator.compare(o1, o2);
        
        return finalComparison;
    }

    
    public void setActionKeyComparator(Comparator<Action> actionKeyComparator) {
        this.actionKeyComparator = actionKeyComparator;
    }
    
    
    @Qualifier("actionKeyComparator")
    @Autowired
    protected Comparator<Action> actionKeyComparator;
}