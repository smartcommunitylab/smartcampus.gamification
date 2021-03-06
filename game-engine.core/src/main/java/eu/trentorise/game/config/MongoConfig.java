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

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

import eu.trentorise.game.core.LogHub;

@Configuration
@EnableMongoRepositories("eu.trentorise.game.repo")
@PropertySource("classpath:engine.core.properties")
public class MongoConfig {

	private final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

	@Autowired
	Environment env;

	@Bean
	public MongoClient mongo() {
		MongoClientFactoryBean mongo = new MongoClientFactoryBean();
		mongo.setHost(env.getProperty("mongo.host"));
		mongo.setPort(env.getProperty("mongo.port", Integer.class));

		 
        final String mongoUsername = env.getProperty("mongo.username");
        final String mongoPwd = env.getProperty("mongo.pwd");
        final String mongoAuthDb = env.getProperty("mongo.authDb");
        if (StringUtils.isNotBlank(mongoUsername) && StringUtils.isNotBlank(mongoPwd)
                && StringUtils.isNotBlank(mongoAuthDb)) {
            LogHub.info(null, logger, "Try an authenticated mongodb connection");
            mongo.setCredentials(Arrays.asList(MongoCredential.createCredential(mongoUsername,
                    mongoAuthDb, mongoPwd.toCharArray())).toArray(new MongoCredential[0]));
        }
		try {
			mongo.afterPropertiesSet();
			return mongo.getObject();
		} catch (Exception e) {
			LogHub.error(null, logger, "Exception in mongo configuration: {}", e.getMessage());
			return null;
		}
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		try {
			MongoTemplate mongoTemplate = new MongoTemplate(
					new SimpleMongoDbFactory(mongo(), env.getProperty("mongo.dbname")));
			return mongoTemplate;
		} catch (Exception e) {
			LogHub.error(null, logger, "Exception in mongotemplate configuration: {}", e.getMessage());
			return null;
		}
	}
}
