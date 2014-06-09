package eu.trentorise.game.rule.model;

/**
 *
 * @author Luca Piras
 */
public class Action {
    
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
        return "Action{" + "id=" + id + ", description=" + description + '}';
    }
}