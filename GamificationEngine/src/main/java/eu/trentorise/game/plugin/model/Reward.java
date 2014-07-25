package eu.trentorise.game.plugin.model;

/**
 *
 * @author Luca Piras
 */
public class Reward {
    
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
        return "Reward{" + "id=" + id + ", name=" + name + '}';
    }
}