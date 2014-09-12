package eu.trentorise.game.ruleengine.model.experimental;

/**
 *
 * @author Luca Piras
 */
public abstract class Plugin {
    
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Plugin{" + "name=" + name + '}';
    }
}