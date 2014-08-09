package eu.trentorise.game.config;

import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.RestHelper;
import eu.trentorise.utils.rest.crud.RestCrudHelper;
import eu.trentorise.utils.rest.crud.RestCrudResponseHelper;
import eu.trentorise.utils.rest.crud.RestCrudResultHelper;
import eu.trentorise.utils.rest.RestResponseHelper;
import eu.trentorise.utils.rest.RestResultHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Luca Piras
 */
@Configuration
public class RestConfig {    
    
    ///////RestHelper///////
    @Bean(name="restHelper")
    public RestHelper restHelper() {
        return new RestHelper();
    }
    
    ///////RestCrudHelper///////
    @Bean(name="restCrudHelper")
    public RestCrudHelper restCrudHelper() {
        return new RestCrudHelper();
    }
    
    
    ///////RestResultHelper///////
    @Bean(name="restResultHelper")
    public RestResultHelper restResultHelper() {
        return new RestResultHelper();
    }
    
    ///////RestCrudResultHelper///////
    @Bean(name="restCrudResultHelper")
    public RestCrudResultHelper restCrudResultHelper() {
        return new RestCrudResultHelper();
    }
    
    
    ///////RestResponseHelper///////
    @Bean(name="restResponseHelper")
    public RestResponseHelper restResponseHelper() {
        return new RestResponseHelper();
    }
    
    ///////RestCrudResponseHelper///////
    @Bean(name="restCrudResponseHelper")
    public RestCrudResponseHelper restCrudResponseHelper() {
        return new RestCrudResponseHelper();
    }
    
    
    ///////RestExceptionHandler///////
    @Bean(name="restExceptionHandler")
    public RestExceptionHandler restExceptionHandler() {
        return new RestExceptionHandler();
    }
}