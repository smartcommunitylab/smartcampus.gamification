package eu.trentorise.game.model;

/**
 *
 * @author Luca Piras
 */
public class Badge {
    
    protected String title;
    
    protected Integer necessaryPoints;

    public Badge() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNecessaryPoints() {
        return necessaryPoints;
    }

    public void setNecessaryPoints(Integer necessaryPoints) {
        this.necessaryPoints = necessaryPoints;
    }

    @Override
    public String toString() {
        return "Badge{" + "title=" + title + ", necessaryPoints=" + necessaryPoints + '}';
    }
}