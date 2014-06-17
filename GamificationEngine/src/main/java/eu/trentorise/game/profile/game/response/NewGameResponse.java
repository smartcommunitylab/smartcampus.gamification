package eu.trentorise.game.profile.game.response;

import eu.trentorise.game.response.*;

/**
 *
 * @author Luca Piras
 */
public class NewGameResponse extends GameResponse {
    
    protected Integer newGameId;

    public Integer getNewGameId() {
        return newGameId;
    }

    public void setNewGameId(Integer newGameId) {
        this.newGameId = newGameId;
    }

    @Override
    public String toString() {
        return "NewGameResponse{" + "newGameId=" + newGameId + '}';
    }
}