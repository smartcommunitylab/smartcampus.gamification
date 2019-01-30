package eu.trentorise.game.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import eu.trentorise.game.managers.GameWorkflow;
import eu.trentorise.game.services.Workflow;


@Configuration
class TestConfiguration {

    /**
     * Use simple gameWorkflow for test purpose, without execution queue and ExecutorService
     * 
     * @return GameWorkflow
     */
    @Bean
    @Primary
    public Workflow workflow() {
        return new GameWorkflow();
    }
}
