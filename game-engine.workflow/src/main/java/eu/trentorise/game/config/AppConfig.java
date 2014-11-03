package eu.trentorise.game.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import eu.trentorise.game.core.AppContextProvider;

@ComponentScan("eu.trentorise.game")
@Configuration
public class AppConfig {

	private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

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
			InputStream propIn = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("quartz.properties");
			if (propIn != null) {
				logger.info("quartz.properties founded");
				Properties props = new Properties();
				props.load(propIn);
				return new StdSchedulerFactory(props).getScheduler();
			} else {
				logger.info("quartz.properties not found");
				return new StdSchedulerFactory().getScheduler();
			}
		} catch (SchedulerException e) {
			logger.error("Error creating scheduler");
			return null;
		} catch (IOException e) {
			logger.error("Error reading scheduler confs");
			return null;
		}
	}
}
