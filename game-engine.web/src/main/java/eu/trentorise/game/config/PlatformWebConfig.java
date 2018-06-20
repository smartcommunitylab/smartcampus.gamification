package eu.trentorise.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import eu.trentorise.game.api.rest.AACAuthenticationInterceptor;
import eu.trentorise.game.platform.PlatformAuthorizationInterceptor;

@Configuration
@Profile("platform")
public class PlatformWebConfig extends WebMvcConfigurerAdapter {


    @Bean
    public HandlerInterceptor platformTokenInterceptor() {
        return new AACAuthenticationInterceptor();
    }

    @Bean
    public HandlerInterceptor platformAuthInterceptor() {
        return new PlatformAuthorizationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(platformTokenInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(platformAuthInterceptor()).addPathPatterns("/api/**");

    }
}
