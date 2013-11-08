package eu.trentorise.game.model;

import java.util.ArrayList;

/**
 *
 * @author luca
 */
public class Player {
    
    protected String username;
    
    protected Integer points;
    
    protected ArrayList<Badge> badges;

    public Player(String username) {
        this.username = username;
        
        this.points = new Integer(0);
        
        this.badges = new ArrayList<>();
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

    public ArrayList<Badge> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<Badge> badges) {
        this.badges = badges;
    }

    @Override
    public String toString() {
        return "Player{" + "username=" + username + ", points=" + points + ", badges=" + badges + '}';
    }
}