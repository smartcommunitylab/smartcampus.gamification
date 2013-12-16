package eu.trentorise.game.profile.model;

/**
 *
 * @author Luca Piras
 */
public class GamificationPlugin {
    
    protected Integer id;
    
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GamificationPlugin{" + "id=" + id + ", description=" + description + '}';
    }
}