package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.ruleengine.model.GameRule;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class GameRuleCollectionResponse {
    
    protected Collection<GameRule> gameRules;

    public Collection<GameRule> getGameRules() {
        return gameRules;
    }

    public void setGameRules(Collection<GameRule> gameRules) {
        this.gameRules = gameRules;
    }

    @Override
    public String toString() {
        return "GameRuleCollectionResponse{" + "gameRules=" + gameRules + '}';
    }
}