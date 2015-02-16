package eu.trentorise.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import eu.trentorise.game.managers.DBPlayerManager;
import eu.trentorise.game.managers.GameWorkflow;
import eu.trentorise.game.managers.QuartzTaskManager;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.services.Workflow;

@Configuration
@Profile("dev")
public class DevConfig {

	@Bean
	public PlayerService playerSrv() {
		return new DBPlayerManager();
	}

	@Bean
	public TaskService taskSrv() {
		return new QuartzTaskManager();
	}

	@Bean
	public Workflow workflow() {
		return new GameWorkflow();
	}

}
