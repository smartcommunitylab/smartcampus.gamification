package eu.trentorise.game.profile.game.comparator;

import eu.trentorise.game.profile.game.model.Game;
import java.util.Comparator;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("gameKeyComparator")
public class GameKeyComparator implements Comparator<Game>{

    @Override
    public int compare(Game o1, Game o2) {
        return o1.getId().compareTo(o2.getId());
    }
}