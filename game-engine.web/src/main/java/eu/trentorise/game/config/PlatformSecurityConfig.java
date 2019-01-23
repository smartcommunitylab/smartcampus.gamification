package eu.trentorise.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerInterceptor;

import eu.trentorise.game.platform.PlatformAuthorizationInterceptor;

@Configuration
@Profile({"platform"})
public class PlatformSecurityConfig {

    @Bean
    public HandlerInterceptor authInterceptor() {
        return new PlatformAuthorizationInterceptor();
    }
}
