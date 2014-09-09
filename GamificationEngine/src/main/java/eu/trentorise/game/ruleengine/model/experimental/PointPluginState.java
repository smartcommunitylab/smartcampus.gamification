package eu.trentorise.game.ruleengine.model.experimental;

/**
 *
 * @author Luca Piras
 */
public class PointPluginState extends PluginState {
    
    protected Integer totalPoints;

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "PointPluginState{" + "totalPoints=" + totalPoints + '}';
    }
}