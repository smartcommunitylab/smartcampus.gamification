package eu.trentorise.game.rule;

import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class Rule {
    
    protected String id;
    
    protected String description;

    protected List<Action> actions;
    protected List<Event> events;
    
    //TODO: id not null
    public Rule(String id, String description) {
        this.id = id;
        this.description = description;
    }
}