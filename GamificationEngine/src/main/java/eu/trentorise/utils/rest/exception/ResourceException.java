package eu.trentorise.utils.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Luca Piras
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceException extends RuntimeException {
    
    public static final String MSG_DEFAULT = "Problems managing a resource!";
    
    public ResourceException(Throwable cause) {
        super(MSG_DEFAULT, cause);
    }
}