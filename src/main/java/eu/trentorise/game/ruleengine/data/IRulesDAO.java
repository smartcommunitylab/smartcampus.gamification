package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.rule.model.Rule;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public interface IRulesDAO {
    
    public List<Rule> getRules(GamificationPluginIdentifier gamificationApproachId);
}