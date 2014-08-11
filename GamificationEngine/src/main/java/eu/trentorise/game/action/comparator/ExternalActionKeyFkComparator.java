package eu.trentorise.game.action.comparator;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.action.model.ExternalAction;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luca Piras
 */
@Component("externalActionKeyFkComparator")
public class ExternalActionKeyFkComparator implements Comparator<ExternalAction> {
    
    @Override
    public int compare(ExternalAction o1, ExternalAction o2) {
        int finalComparison = actionKeyComparator.compare(o1, o2);
        if (0 == finalComparison) {
            finalComparison = applicationKeyComparator.compare(o1.getApplication(), 
                                                               o2.getApplication());
        }
        
        return finalComparison;
    }

    
    public void setActionKeyComparator(Comparator<Action> actionKeyComparator) {
        this.actionKeyComparator = actionKeyComparator;
    }

    public void setApplicationKeyComparator(Comparator<Application> applicationKeyComparator) {
        this.applicationKeyComparator = applicationKeyComparator;
    }
    
    
    @Qualifier("actionKeyComparator")
    @Autowired
    protected Comparator<Action> actionKeyComparator;
    
    @Qualifier("applicationKeyComparator")
    @Autowired
    protected Comparator<Application> applicationKeyComparator;
}