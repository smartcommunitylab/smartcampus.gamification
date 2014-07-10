package eu.trentorise.game.profile.game.response;

import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.response.*;

/**
 *
 * @author Luca Piras
 */
public class NewGameResponse extends GameResponse {
    
    protected Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "NewGameResponse{" + "game=" + game + '}';
    }
}