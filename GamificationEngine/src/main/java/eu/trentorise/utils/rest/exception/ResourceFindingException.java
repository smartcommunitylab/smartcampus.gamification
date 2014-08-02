package eu.trentorise.utils.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Luca Piras
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceFindingException extends RuntimeException {
    
    public static final String MSG_DEFAULT = "Resource not found or problems finding a resource!";
    
    
    public ResourceFindingException(Throwable cause) {
        super(MSG_DEFAULT, cause);
    }
}