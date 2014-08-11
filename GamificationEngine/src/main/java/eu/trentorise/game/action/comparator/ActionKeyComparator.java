package eu.trentorise.game.action.comparator;

import eu.trentorise.game.action.model.Action;
import java.util.Comparator;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("actionKeyComparator")
public class ActionKeyComparator implements Comparator<Action>{

    @Override
    public int compare(Action o1, Action o2) {
        return o1.getId().compareTo(o2.getId());
    }
}