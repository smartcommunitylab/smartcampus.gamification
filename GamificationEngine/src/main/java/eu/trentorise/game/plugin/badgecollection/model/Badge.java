package eu.trentorise.game.plugin.badgecollection.model;

import eu.trentorise.game.plugin.model.Reward;
import eu.trentorise.utils.web.WebFile;

/**
 *
 * @author Luca Piras
 */
public class Badge extends Reward {
    
    protected BadgeCollectionPlugin badgeCollection;
    
    protected WebFile image;

    public BadgeCollectionPlugin getBadgeCollection() {
        return badgeCollection;
    }

    public void setBadgeCollection(BadgeCollectionPlugin badgeCollection) {
        this.badgeCollection = badgeCollection;
    }

    public WebFile getImage() {
        return image;
    }

    public void setImage(WebFile image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Badge{" + "badgeCollection=" + badgeCollection + ", image=" + image + " " + super.toString() + " " + '}';
    }
}