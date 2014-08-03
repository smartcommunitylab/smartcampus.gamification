package eu.trentorise.game.config;

import eu.trentorise.game.action.model.Application;
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
    
    
    ///////restResourceHelper///////
    @Bean(name="applicationRestCrudHelper")
    public RestCrudHelper<Application, Object, Application> applicationRestCrudHelper() {
        return new RestCrudHelper<>();
    }
}