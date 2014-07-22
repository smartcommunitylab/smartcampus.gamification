package eu.trentorise.game.application.model;

/**
 *
 * @author Luca Piras
 */
public class BasicParam extends Param {
    
    protected ParamType type;

    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BasicParam{" + "type=" + type + " " + super.toString() + " " + '}';
    }
}