package eu.trentorise.game.action.model;

import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class GameInternalAction {
    
    protected Game game;
    
    protected InternalAction internalAction;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public InternalAction getInternalAction() {
        return internalAction;
    }

    public void setInternalAction(InternalAction internalAction) {
        this.internalAction = internalAction;
    }

    @Override
    public String toString() {
        return "GameInternalAction{" + "game=" + game + ", internalAction=" + internalAction + '}';
    }
}