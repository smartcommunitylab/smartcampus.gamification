package eu.trentorise.game.application.controller.comparator;

import eu.trentorise.game.action.model.Application;
import java.util.Comparator;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("applicationKeyComparator")
public class ApplicationKeyComparator implements Comparator<Application>{

    @Override
    public int compare(Application o1, Application o2) {
        return o1.getId().compareTo(o2.getId());
    }
}