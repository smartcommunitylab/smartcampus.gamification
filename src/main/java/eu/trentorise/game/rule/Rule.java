package eu.trentorise.game.rule;

import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class Rule {
    
    protected String id;
    
    protected String description;
    
    protected String content;

    protected List<Action> actions;
    protected List<Event> events;
    
    //TODO: id not null maybe throw exception
    public Rule(String id, String description, String content) {
        this.id = id;
        this.description = description;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}