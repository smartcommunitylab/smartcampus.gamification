package eu.trentorise.game.rule.model;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplate {
    
    protected Integer id;
    protected GamificationPlugin plugin;
    
    //TODO: this one has to be unique
    protected String name;
    
    protected Type type;
    
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GamificationPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(GamificationPlugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TemplateRule{" + "id=" + id + ", plugin=" + plugin + ", name=" + name + ", type=" + type + ", description=" + description + '}';
    }
}