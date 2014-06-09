package eu.trentorise.game.rule.model;

/**
 *
 * @author luca
 */
public class Event {
    
    protected String id;
    
    protected String description;

    //TODO: id not null
    public Event(String id, String description) {
        this.id = id;
        this.description = description;
    }
}