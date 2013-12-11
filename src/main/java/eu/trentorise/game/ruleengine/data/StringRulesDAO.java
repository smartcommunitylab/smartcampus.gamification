package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.rule.Rule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class StringRulesDAO implements IRulesDAO {
    
    @Override
    public List<Rule> getRules(Integer gamificationApproachId) {
        List<Rule> rules = new ArrayList<>();
        
        String myRule = this.buildBadgeRule("Basic badge", "10", "Basic Mayor");
        Rule rule = new Rule("1", null, myRule);
        rules.add(rule);
        
        myRule = this.buildBadgeRule("Enhanced badge", "100", "Enhanced Mayor");
        rule = new Rule("2", null, myRule);
        rules.add(rule);
        
        return rules;
    }
    
    protected String buildBadgeRule(String ruleName, String points, 
                                    String badgeTitle) {
        
        StringBuilder sb = new StringBuilder("import eu.trentorise.game.model.player.Player ");
        sb.append("import eu.trentorise.game.model.backpack.Badge ");
        sb.append("import java.util.ArrayList ");
        sb.append("rule \"").append(ruleName).append("\" ");
        sb.append("when player:Player(points==\"").append(points).append("\") ");
        sb.append("then Badge badge = new Badge(\"").append(badgeTitle).append("\", ").append(points).append("); ");
        sb.append("player.getBadges().add(badge); end");
        
        return sb.toString();
    }
}