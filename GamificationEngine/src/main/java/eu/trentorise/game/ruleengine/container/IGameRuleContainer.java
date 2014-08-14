package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.ruleengine.model.Rule;

/**
 *
 * @author Luca Piras
 */
public interface IGameRuleContainer {
    
    public Game getGame();
    public void setGame(Game game);

    public Rule getRule();
    public void setRule(Rule rule);
}