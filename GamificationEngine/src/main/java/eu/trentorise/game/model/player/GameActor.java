package eu.trentorise.game.model.player;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author Luca Piras
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GameActor implements Serializable {
    
    @Id
    protected String username;
    
    protected Integer points;
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        if (null != points) {
            this.points = points;
        }
    }
}