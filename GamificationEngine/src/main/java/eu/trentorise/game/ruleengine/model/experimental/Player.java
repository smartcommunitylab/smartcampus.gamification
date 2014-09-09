package eu.trentorise.game.ruleengine.model.experimental;

/**
 *
 * @author Luca Piras
 */
public class Player {
    
    protected Integer id;
    //key gameId
    //Map<int, PlayerState> states;
    protected Integer totalPoints;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", totalPoints=" + totalPoints + '}';
    }
}