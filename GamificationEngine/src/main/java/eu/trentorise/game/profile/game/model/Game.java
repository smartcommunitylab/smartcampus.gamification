package eu.trentorise.game.profile.game.model;

/**
 *
 * @author Luca Piras
 */
public class Game {
    
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
        return "Game{" + "id=" + id + ", name=" + name + '}';
    }
}