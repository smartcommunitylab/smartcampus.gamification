package eu.trentorise.game.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import eu.trentorise.game.core.AppContextProvider;

@ComponentScan("eu.trentorise.game")
@Configuration
public class AppConfig {

	@Bean
	public ThreadPoolTaskScheduler scheduler() {
		return new ThreadPoolTaskScheduler();
	}

	@Bean
	public AppContextProvider appCtxProvider() {
		return new AppContextProvider();
	}

	@Bean
	public Scheduler quartzScheduler() {
		try {
			//
			return new StdSchedulerFactory("engine.properties").getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
