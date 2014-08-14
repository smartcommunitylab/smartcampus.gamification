package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.ruleengine.model.GameRule;

/**
 *
 * @author Luca Piras
 */
public class GameRuleResponse {

    protected GameRule gameRule;

    public GameRule getGameRule() {
        return gameRule;
    }

    public void setGameRule(GameRule gameRule) {
        this.gameRule = gameRule;
    }

    @Override
    public String toString() {
        return "GameRuleResponse{" + "gameRule=" + gameRule + '}';
    }
}