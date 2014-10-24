package eu.trentorise.game.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;

@Configuration
@EnableMongoRepositories("eu.trentorise.game.repo")
@PropertySource("classpath:engine.properties")
public class MongoConfig {

	private final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

	@Autowired
	Environment env;

	@Bean
	public Mongo mongo() {
		MongoFactoryBean mongo = new MongoFactoryBean();
		mongo.setHost(env.getProperty("mongo.host"));
		mongo.setPort(env.getProperty("mongo.port", Integer.class));
		try {
			mongo.afterPropertiesSet();
			return mongo.getObject();
		} catch (Exception e) {
			logger.error("Exception in mongo configuration: {}", e.getMessage());
			return null;
		}
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		try {
			MongoTemplate mongoTemplate = new MongoTemplate(
					new SimpleMongoDbFactory(mongo(),
							env.getProperty("mongo.dbname")));
			return mongoTemplate;
		} catch (Exception e) {
			logger.error("Exception in mongotemplate configuration: {}",
					e.getMessage());
			return null;
		}
	}
}
