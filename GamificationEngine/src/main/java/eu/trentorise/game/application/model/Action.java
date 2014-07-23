package eu.trentorise.game.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Luca Piras
 */
//Necessary for the Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
    
    protected Integer id;
    
    protected String name;
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        return "Action{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }
}