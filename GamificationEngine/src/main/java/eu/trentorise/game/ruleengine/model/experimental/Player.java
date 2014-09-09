package eu.trentorise.game.ruleengine.model.experimental;

import java.util.Map;

/**
 *
 * @author Luca Piras
 */
public class Player {
    
    protected Integer id;
    
    //Integer gameId
    protected Map<Integer, PlayerState> gameStates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<Integer, PlayerState> getGameStates() {
        return gameStates;
    }

    public void setGameStates(Map<Integer, PlayerState> gameStates) {
        this.gameStates = gameStates;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", gameStates=" + gameStates + '}';
    }
}