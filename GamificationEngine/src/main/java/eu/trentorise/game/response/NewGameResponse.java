package eu.trentorise.game.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.profile.game.model.Game;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewGameResponse {
    
    protected Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "GameResponse{" + "game=" + game + '}';
    }
}