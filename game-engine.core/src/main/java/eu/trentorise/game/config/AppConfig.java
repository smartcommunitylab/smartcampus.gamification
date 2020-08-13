/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.config;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.Clock;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.SystemClock;
import eu.trentorise.game.managers.*;
import eu.trentorise.game.services.*;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@ComponentScan("eu.trentorise.game")
@Configuration
@EnableScheduling
@PropertySource("classpath:engine.core.properties")
public class AppConfig {

	private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	private Environment env;

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
			boolean activatePersistence = env.getProperty("task.persistence.activate", Boolean.class, false);
			if (activatePersistence) {
				LogHub.info(null, logger, "task persistence active..load quartz properties");
				Properties props = new Properties();
				Map<String, Object> propsMap = new HashMap<String, Object>();
				propsMap.put("org.quartz.jobStore.class",
						env.getProperty("org.quartz.jobStore.class", "com.novemberain.quartz.mongodb.MongoDBJobStore"));
				propsMap.put("org.quartz.jobStore.addresses",
						env.getProperty("org.quartz.jobStore.addresses", "localhost"));
				propsMap.put("org.quartz.jobStore.dbName",
						env.getProperty("org.quartz.jobStore.dbName", "gamification_task_store"));
				propsMap.put("org.quartz.jobStore.collectionPrefix",
						env.getProperty("org.quartz.jobStore.collectionPrefix", "quartz"));
				propsMap.put("org.quartz.threadPool.threadCount",
						env.getProperty("org.quartz.threadPool.threadCount", "10"));
				
                propsMap.put("org.quartz.jobStore.isClustered",
                        env.getProperty("org.quartz.jobStore.isClustered", "true"));

                propsMap.put("rg.quartz.scheduler.instanceId",
                        env.getProperty("rg.quartz.scheduler.instanceId", "AUTO"));
                propsMap.put("org.quartz.scheduler.instanceName", env
                        .getProperty("org.quartz.scheduler.instanceName", "gamificationCluster"));
                propsMap.put("org.quartz.jobStore.clusterCheckinInterval",
                        env.getProperty("org.quartz.jobStore.clusterCheckinInterval", "10000"));

                propsMap.put("org.quartz.jobStore.triggerTimeoutMillis",
                        env.getProperty("org.quartz.jobStore.triggerTimeoutMillis", "1200000"));
                propsMap.put("org.quartz.jobStore.jobTimeoutMillis",
                        env.getProperty("org.quartz.jobStore.jobTimeoutMillis", "1200000"));
                propsMap.put("org.quartz.jobStore.misfireThreshold",
                        env.getProperty("org.quartz.jobStore.misfireThreshold", "10000"));
                propsMap.put("org.quartz.jobStore.mongoOptionWriteConcernTimeoutMillis",
                        env.getProperty("org.quartz.jobStore.mongoOptionWriteConcernTimeoutMillis",
                                "10000"));
                /*
                 * Exception if the line below is uncommented
                 */
                // propsMap.put("org.quartz.jobStore.checkInErrorHandler.class",
                // env.getProperty("org.quartz.jobStore.checkInErrorHandler.class",
                // "com.novemberain.quartz.mongodb.cluster.NoOpErrorHandler"));

				props.putAll(propsMap);
				return new StdSchedulerFactory(props).getScheduler();
			} else {
				LogHub.info(null, logger, "task persistence unactive");
				return new StdSchedulerFactory().getScheduler();

			}
		} catch (SchedulerException e) {
			LogHub.error(null, logger, "Error creating scheduler");
			return null;
		}

	}

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
		return new QueueGameWorkflow();
	}

    @Bean
    public Clock clock() {
        return new SystemClock();
    }

	@Bean
	public GameService gameSrv(){
		return new GameManager();
	}

	@Bean
	public GameEngine engine(){
		return new DroolsEngine();
	}

	@Bean
	public TraceService traceSrv(){
		return new TraceManager();
	}
}
