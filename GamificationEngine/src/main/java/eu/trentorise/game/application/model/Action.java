package eu.trentorise.game.application.model;

import eu.trentorise.game.application.model.Application;

/**
 *
 * @author Luca Piras
 */
public class Action {
    
    protected Integer id;
    
    protected Application application;
    
    protected String name;
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
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
        return "Action{" + "id=" + id + ", application=" + application + ", name=" + name + ", description=" + description + '}';
    }
}