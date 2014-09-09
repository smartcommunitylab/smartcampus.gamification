package eu.trentorise.game.ruleengine.model.experimental;

/**
 *
 * @author Luca Piras
 */
public class Badge {
    protected Integer id;
    protected String name;
    protected String icon;
    protected String timestamp;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Badge{" + "id=" + id + ", name=" + name + ", icon=" + icon + ", timestamp=" + timestamp + '}';
    }
}