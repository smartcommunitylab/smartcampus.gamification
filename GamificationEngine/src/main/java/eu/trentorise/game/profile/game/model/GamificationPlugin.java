package eu.trentorise.game.profile.game.model;

/**
 *
 * @author Luca Piras
 */
public class GamificationPlugin {
    
    //TODO: this is the key
    protected String name;
    
    protected String description;

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    @Override
    public String toString() {
        return "GamificationPlugin{" + "name=" + name + ", description=" + description + '}';
    }
}