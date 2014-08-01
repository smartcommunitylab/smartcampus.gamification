package eu.trentorise.game.action.model;

/**
 *
 * @author Luca Piras
 */
public class Application {
    
    protected Integer id;

    protected String name;
    
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

    @Override
    public String toString() {
        return "Application{" + "id=" + id + ", name=" + name + '}';
    }
}