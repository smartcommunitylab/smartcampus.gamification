package eu.trentorise.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.HandlerInterceptor;

import eu.trentorise.game.api.rest.AACAuthenticationInterceptor;
import eu.trentorise.game.platform.PlatformAuthorizationInterceptor;
import eu.trentorise.game.service.IdentityLookupService;
import eu.trentorise.game.service.SpringSecurityIdentityLookup;

@Configuration
@Profile("platform")
// @PropertySource("classpath:engine.web.properties")
public class PlatformSecConfig extends WebSecurityConfigurerAdapter {

    // @Resource
    // private Environment env;

    // @Bean
    // public AACService aacService() {
    // return new AACService(env.getProperty("aac.url"), env.getProperty("aac.clientId"),
    // env.getProperty("aac.clientSecret"));
    // }

    @Bean
    public HandlerInterceptor authInterceptor() {
        return new PlatformAuthorizationInterceptor();
    }

    // @Bean
    // public AACProfileService aacProfileService() {
    // return new AACProfileService(env.getProperty("aac.url"));
    // }

    @Bean
    public HandlerInterceptor tokenInterceptor() {
        return new AACAuthenticationInterceptor();
    }


    @Bean
    public IdentityLookupService identityLookup() {
        return new SpringSecurityIdentityLookup();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // application never creates an http session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/gengine/**", "/console/**", "/model/**", "/data/**",
                "/notification/**", "/exec/**").anonymous();


        // disable csrf permits POST http call to ConsoleController
        // without using csrf token
        http.csrf().disable();
    }

}
