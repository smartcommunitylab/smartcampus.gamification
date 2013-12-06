package eu.trentorise.game.model.backpack;

/**
 *
 * @author Luca Piras
 */
public class Badge {
    
    protected String title;
    
    protected Integer necessaryPoints;

    //TODO: improve the title attribute with a default element and create the
    //test case
    public Badge(String title, Integer necessaryPoints) {
        this.title = title;
        
        if (necessaryPoints.compareTo(new Integer(0)) < 0) {
            this.necessaryPoints = necessaryPoints * (- 1);
        } else {
            this.necessaryPoints = necessaryPoints;
        }
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