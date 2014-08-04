package eu.trentorise.game.config;

import eu.trentorise.utils.rest.RestCrudHelper;
import eu.trentorise.utils.rest.RestResultHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Luca Piras
 */
@Configuration
public class RestConfig {
    
    ///////RestResultHelper///////
    @Bean(name="restResultHelper")
    public RestResultHelper restResultHelper() {
        return new RestResultHelper();
    }
    
    
    ///////RestCrudHelper///////
    @Bean(name="restCrudHelper")
    public RestCrudHelper restCrudHelper() {
        return new RestCrudHelper();
    }
}