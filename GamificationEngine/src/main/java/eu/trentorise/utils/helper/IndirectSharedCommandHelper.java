package eu.trentorise.utils.helper;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Luca Piras
 */
public class IndirectSharedCommandHelper extends AbstractCommandHelper<Object, HttpSession> {
    
    @Override
    public void setCommand(Object command, HttpSession session) {
        session.setAttribute(this.commandName, command);
    }

    @Override
    public Object getCommand(HttpSession session) {
        return session.getAttribute(this.commandName);
    }
}