package eu.trentorise.game.ruleengine.model.experimental;

/**
 *
 * @author Luca Piras
 */
public abstract class Plugin {
    
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
        return "Plugin{" + "id=" + id + ", name=" + name + '}';
    }
}