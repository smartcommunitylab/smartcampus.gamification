package eu.trentorise.game.action.comparator;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.Param;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("paramKeyComparator")
public class ParamKeyComparator implements Comparator<Param> {
    
    @Override
    public int compare(Param o1, Param o2) {
        int finalComparison = comparator.compare(o1.getAction(), o2.getAction());
        if (0 == finalComparison) {
            finalComparison = o1.getName().compareToIgnoreCase(o2.getName());
        }
        
        return finalComparison;
    }

    
    public void setComparator(Comparator<Action> comparator) {
        this.comparator = comparator;
    }
    
    
    @Qualifier("actionKeyComparator")
    @Autowired
    protected Comparator<Action> comparator;
}