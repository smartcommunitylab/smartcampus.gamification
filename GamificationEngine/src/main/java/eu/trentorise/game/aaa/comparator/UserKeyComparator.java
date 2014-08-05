package eu.trentorise.game.aaa.comparator;

import eu.trentorise.game.aaa.model.User;
import java.util.Comparator;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("userKeyComparator")
public class UserKeyComparator implements Comparator<User>{

    @Override
    public int compare(User o1, User o2) {
        return o1.getUsername().compareToIgnoreCase(o2.getUsername());
    }
}