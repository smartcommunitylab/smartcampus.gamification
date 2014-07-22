package eu.trentorise.game.application.model;

/**
 *
 * @author Luca Piras
 */
public class BasicParam {
    
    protected String name;
    protected Action action;
    
    protected ParamType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BasicParam{" + "name=" + name + ", action=" + action + ", type=" + type + '}';
    }
}