package eu.trentorise.game.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import eu.trentorise.game.managers.GameWorkflow;
import eu.trentorise.game.services.Workflow;

@Configuration
public class TestCoreConfiguration {

    @Bean
    @Primary
    public Workflow workflow() {
        return new GameWorkflow();
    }
}