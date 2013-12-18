package eu.trentorise.game.model.player;

import eu.trentorise.game.model.backpack.Badge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author luca
 */
public class Player extends GameActor {
    
    protected List<Badge> badges;
    
    
    public Player() {
        this.points = new Integer(0);
        
        this.badges = Collections.synchronizedList(new ArrayList<Badge>());
    }
    
    
    public Integer getBadgesCount() {
        return this.badges.size();
    }
    
    
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

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    
    @Override
    public String toString() {
        return "Player{" + "username=" + username + ", points=" + points + ", badges=" + badges + '}';
    }
}