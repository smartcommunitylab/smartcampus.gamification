package eu.trentorise.game.ruleengine.data.drools;

import eu.trentorise.game.ruleengine.data.AbstractRulesDAO;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class DroolsStringRulesDAO extends AbstractRulesDAO {
    
    @Override
    protected List<String> obtainsContentRules(List<String> contentRules) {
        String rule = this.buildBadgeRule("Basic badge", "10", "Basic Mayor");
        contentRules.add(rule);
        
        rule = this.buildBadgeRule("Enhanced badge", "100", "Enhanced Mayor");
        contentRules.add(rule);
        
        return contentRules;
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