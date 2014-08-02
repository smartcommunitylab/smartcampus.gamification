package eu.trentorise.game.config;

import eu.trentorise.utils.rest.RestResponseHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Luca Piras
 */
@Configuration
public class RestConfig {
    
    ///////restResponseHelper///////
    @Bean(name="restResponseHelper")
    public RestResponseHelper restResponseHelper() {
        return new RestResponseHelper();
    }
}