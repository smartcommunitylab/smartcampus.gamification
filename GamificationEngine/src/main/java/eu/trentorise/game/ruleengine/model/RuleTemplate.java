package eu.trentorise.game.ruleengine.model;

import eu.trentorise.game.plugin.model.Plugin;

/**
 *
 * @author Luca Piras
 */
public class RuleTemplate {
    
    protected Integer id;
    protected Plugin plugin;
    
    //TODO: this one has to be unique
    protected String name;
    
    protected RuleTemplateType type;
    
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RuleTemplateType getType() {
        return type;
    }

    public void setType(RuleTemplateType type) {
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
        return "RuleTemplate{" + "id=" + id + ", plugin=" + plugin + ", name=" + name + ", type=" + type + ", description=" + description + '}';
    }
}