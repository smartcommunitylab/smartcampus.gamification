package eu.trentorise.game.validator;

import eu.trentorise.game.model.Badge;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Luca Piras
 */
@Component("badgeValidator")
@Scope("prototype")
public class BadgeValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return Badge.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "game.field.empty");
    }
}