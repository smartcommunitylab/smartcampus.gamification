package eu.trentorise.game.ruleengine.model.experimental;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Luca Piras
 */
public class PointPluginState extends PluginState {
    
    protected Integer totalPoints;

    private final PropertyChangeSupport changes = new PropertyChangeSupport( this );
    
    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(final Integer newTotalPoints) {
        Integer oldTotalPoints = this.totalPoints;
        this.totalPoints = newTotalPoints;
        
        this.changes.firePropertyChange("totalPoints", oldTotalPoints,
                                        newTotalPoints);
    }
    
    
    public void addPropertyChangeListener(final PropertyChangeListener l) {
        this.changes.addPropertyChangeListener( l );
    }

    public void removePropertyChangeListener(final PropertyChangeListener l) {
        this.changes.removePropertyChangeListener( l );
    }
    
    
    @Override
    public String toString() {
        return "PointPluginState{" + "totalPoints=" + totalPoints + '}';
    }
}