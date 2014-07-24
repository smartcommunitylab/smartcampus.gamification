package eu.trentorise.game.ruleengine.request;

import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.ruleengine.model.Rule;

/**
 *
 * @author Luca Piras
 */
public class RuleRequest {
    
    protected Game game;
    
    protected Rule rule;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "RuleRequest{" + "game=" + game + ", rule=" + rule + '}';
    }
}