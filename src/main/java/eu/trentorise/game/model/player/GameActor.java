package eu.trentorise.game.model.player;

/**
 *
 * @author luca
 */
public class GameActor {
    
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