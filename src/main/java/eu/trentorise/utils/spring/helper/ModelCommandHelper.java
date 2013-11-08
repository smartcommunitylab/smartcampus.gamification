package eu.trentorise.utils.spring.helper;

import eu.trentorise.utils.helper.AbstractCommandHelper;
import org.springframework.ui.Model;

/**
 *
 * @author Luca Piras
 */
public class ModelCommandHelper extends AbstractCommandHelper<Object, Model> {
    
    @Override
    public void setCommand(Object command, Model model) {
        model.addAttribute(this.commandName, command);
    }

    @Override
    public Object getCommand(Model model) {
        return model.asMap().get(this.commandName);
    }
}