package eu.trentorise.game.plugin.badgecollection.response;

import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.response.*;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class BadgeListResponse extends GameResponse {
    
    protected List<Badge> badges;

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    @Override
    public String toString() {
        return "BadgeListResponse{" + "badges=" + badges + '}';
    }
}