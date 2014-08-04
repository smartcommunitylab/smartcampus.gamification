package eu.trentorise.game.response;

import eu.trentorise.game.profile.game.model.Game;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class GameCollectionResponse {
    
    protected Collection<Game> games;

    public Collection<Game> getGames() {
        return games;
    }

    public void setGames(Collection<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "GameCollectionResponse{" + "games=" + games + '}';
    }
}