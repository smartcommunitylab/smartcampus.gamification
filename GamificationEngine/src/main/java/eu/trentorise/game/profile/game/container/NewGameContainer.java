package eu.trentorise.game.profile.game.container;

import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
public class NewGameContainer implements INewGameContainer {
    
    protected Game game;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "NewGameContainer{" + "game=" + game + '}';
    }
}