package eu.trentorise.game.plugin.badgecollection.model;

import eu.trentorise.utils.web.WebFile;

/**
 *
 * @author Luca Piras
 */
public class Badge {
    
    protected Integer id;
    
    protected BadgeCollectionPlugin badgeCollection;
    
    protected WebFile image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        return "Badge{" + "id=" + id + ", badgeCollection=" + badgeCollection + ", image=" + image + '}';
    }
}