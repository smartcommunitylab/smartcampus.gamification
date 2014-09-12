package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.ruleengine.model.Rule;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public interface IRulesDAO {
    
    public List<Rule> getRules(List<Rule> rules);
}