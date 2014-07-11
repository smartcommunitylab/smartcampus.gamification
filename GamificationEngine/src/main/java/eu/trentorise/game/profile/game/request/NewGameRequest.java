package eu.trentorise.game.profile.game.request;

import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class NewGameRequest {
    
    protected Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "NewGameCO{" + "game=" + game + '}';
    }
}