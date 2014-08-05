package eu.trentorise.game.aaa.comparator;

import eu.trentorise.game.aaa.model.User;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("userComparator")
public class UserComparator implements Comparator<User>{

    @Override
    public int compare(User o1, User o2) {
        int finalComparison = userKeyComparator.compare(o1, o2);
        if (0 == finalComparison) {
            finalComparison = o1.getPassword().compareTo(o2.getPassword());
        }
        
        return finalComparison;
    }
    
    @Qualifier("userKeyComparator")
    @Autowired
    protected Comparator<User> userKeyComparator;
}