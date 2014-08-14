package eu.trentorise.game.ruleengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
//Necessary for the Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameRule {
    
    protected Game game;
    
    protected Rule rule;

    protected Boolean activated;

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

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "GameRule{" + "game=" + game + ", rule=" + rule + ", activated=" + activated + '}';
    }
}