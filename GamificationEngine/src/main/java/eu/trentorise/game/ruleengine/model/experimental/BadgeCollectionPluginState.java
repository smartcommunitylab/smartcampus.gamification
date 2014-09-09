package eu.trentorise.game.ruleengine.model.experimental;

import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionPluginState extends PluginState {
    protected Collection<Badge> earnedBadges;

    public Collection<Badge> getEarnedBadges() {
        return earnedBadges;
    }

    public void setEarnedBadges(Collection<Badge> earnedBadges) {
        this.earnedBadges = earnedBadges;
    }

    @Override
    public String toString() {
        return "BadgeCollectionPluginState{" + "earnedBadges=" + earnedBadges + '}';
    }
}