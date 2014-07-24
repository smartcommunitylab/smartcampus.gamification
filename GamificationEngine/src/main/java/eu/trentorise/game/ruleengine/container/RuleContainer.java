package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.ruleengine.model.Rule;

/**
 *
 * @author Luca Piras
 */
public class RuleContainer implements IRuleContainer {
    
    protected Game game;
    
    protected Rule rule;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "RuleContainer{" + "game=" + game + ", rule=" + rule + '}';
    }
}