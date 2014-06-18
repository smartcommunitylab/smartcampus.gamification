package eu.trentorise.game.rule.model;

import eu.trentorise.game.profile.game.model.GameProfile;
import eu.trentorise.game.plugin.model.GamificationPlugin;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class Rule {
    
    protected Integer id;
    //TODO: templateRule and gamificationPlugin together have to be unique
    protected TemplateRule templateRule;
    protected GamificationPlugin gamificationPlugin;
    protected GameProfile gameProfile;
    
    protected String description;
    
    protected String content;

    protected List<Action> actions;
    protected List<Event> events;
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TemplateRule getTemplateRule() {
        return templateRule;
    }

    public void setTemplateRule(TemplateRule templateRule) {
        this.templateRule = templateRule;
    }

    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(GamificationPlugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Rule{" + "id=" + id + ", templateRule=" + templateRule + ", gamificationPlugin=" + gamificationPlugin + ", gameProfile=" + gameProfile + ", description=" + description + ", content=" + content + ", actions=" + actions + ", events=" + events + '}';
    }
}