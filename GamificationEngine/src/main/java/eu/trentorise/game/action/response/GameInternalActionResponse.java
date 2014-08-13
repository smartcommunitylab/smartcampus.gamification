package eu.trentorise.game.action.response;

import eu.trentorise.game.action.model.GameInternalAction;

/**
 *
 * @author Luca Piras
 */
public class GameInternalActionResponse {
    
    protected GameInternalAction gameInternalAction;

    public GameInternalAction getGameInternalAction() {
        return gameInternalAction;
    }

    public void setGameInternalAction(GameInternalAction gameInternalAction) {
        this.gameInternalAction = gameInternalAction;
    }

    @Override
    public String toString() {
        return "GameInternalActionResponse{" + "gameInternalAction=" + gameInternalAction + '}';
    }
}