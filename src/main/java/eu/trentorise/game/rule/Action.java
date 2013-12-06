package eu.trentorise.game.rule;

/**
 *
 * @author Luca Piras
 */
public class Action {
    
    protected String id;
    
    protected String description;

    //TODO: id not null
    public Action(String id, String description) {
        this.id = id;
        this.description = description;
    }
}