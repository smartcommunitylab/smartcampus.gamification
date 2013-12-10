package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.rule.Rule;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public interface IRulesDAO {
    
    public List<Rule> getRules(Integer gamificationApproachId);
}