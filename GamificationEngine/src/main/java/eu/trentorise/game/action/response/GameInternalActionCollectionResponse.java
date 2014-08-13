package eu.trentorise.game.action.response;

import eu.trentorise.game.action.model.GameInternalAction;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class GameInternalActionCollectionResponse {
    
    protected Collection<GameInternalAction> gameInternalActions;

    public Collection<GameInternalAction> getGameInternalActions() {
        return gameInternalActions;
    }

    public void setGameInternalActions(Collection<GameInternalAction> gameInternalActions) {
        this.gameInternalActions = gameInternalActions;
    }

    @Override
    public String toString() {
        return "GameInternalActionCollectionResponse{" + "gameInternalActions=" + gameInternalActions + '}';
    }
}