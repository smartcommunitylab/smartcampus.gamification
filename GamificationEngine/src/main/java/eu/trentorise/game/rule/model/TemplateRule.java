package eu.trentorise.game.rule.model;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public class TemplateRule {
    
    protected Integer id;
    protected GamificationPlugin plugin;

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

    @Override
    public String toString() {
        return "TemplateRule{" + "id=" + id + ", plugin=" + plugin + '}';
    }
}