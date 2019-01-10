package eu.trentorise.game.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import eu.trentorise.game.managers.GameWorkflow;
import eu.trentorise.game.services.Workflow;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    public Workflow workflow() {
        return new GameWorkflow();
    }
}
