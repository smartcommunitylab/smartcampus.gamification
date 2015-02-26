package eu.trentorise.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
 * extend WebMvcConfigurerAdapter and not use annotation @EnableMvc to permit
 * correct static resources publishing and restController functionalities
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

}
