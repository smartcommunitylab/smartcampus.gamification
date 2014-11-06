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
					.getResourceAsStream("task.persistence.properties");
			boolean activatePersistence = false;
			Properties props = new Properties();
			if (propIn != null) {
				logger.info("quartz.properties founded");
				props.load(propIn);
				if ("true".equalsIgnoreCase(props.getProperty(
						"task.persistence.activate", "false"))) {
					activatePersistence = true;
					logger.info("task.persistence.activate conf setted to TRUE");
				} else {
					logger.info("task.persistence.activate conf setted to FALSE or not setted");
				}
			} else {
				logger.info("quartz.properties not found");
			}
			if (activatePersistence) {
				logger.info("task persistence active");
				return new StdSchedulerFactory(props).getScheduler();
			} else {
				logger.info("task persistence unactive");
				return new StdSchedulerFactory().getScheduler();

			}
		} catch (SchedulerException e) {
			logger.error("Error creating scheduler");
			return null;
		} catch (IOException e) {
			logger.error("Error loading scheduler props");
			return null;
		}

	}
}
