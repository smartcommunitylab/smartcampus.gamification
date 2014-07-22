package eu.trentorise.game.application.model;

/**
 *
 * @author Luca Piras
 */
public class Application {
    
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Application{" + "id=" + id + '}';
    }
}