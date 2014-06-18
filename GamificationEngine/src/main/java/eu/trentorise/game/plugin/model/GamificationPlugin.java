package eu.trentorise.game.plugin.model;

/**
 *
 * @author Luca Piras
 */
public class GamificationPlugin {
    
    //TODO: this is the key
    protected String name;
    
    protected String version;
    
    protected String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GamificationPlugin{" + "name=" + name + ", version=" + version + ", description=" + description + '}';
    }
}