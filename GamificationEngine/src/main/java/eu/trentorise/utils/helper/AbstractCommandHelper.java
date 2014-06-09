package eu.trentorise.utils.helper;

/**
 *
 * @author Luca Piras
 */
public abstract class AbstractCommandHelper<C, P> {
    
    protected String commandName;
    
    public abstract void setCommand(C command, P place);
    
    public abstract C getCommand(P place);

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
}