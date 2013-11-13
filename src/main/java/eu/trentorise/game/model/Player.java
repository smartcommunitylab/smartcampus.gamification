package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author luca
 */
public class Player {
    
    public static final String USERNAME_DEFAULT = "DEFAULT";
    
    protected String username;
    
    protected Integer points;
    
    protected List<Badge> badges;

    public Player() {
        this.init(username);
    }
    
    public Player(String username) {
        this.init(username);
    }
    
    private void init(String username) {
        this.username = username;
        
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
        this.points = points;
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