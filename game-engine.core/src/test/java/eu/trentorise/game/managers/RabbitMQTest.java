package eu.trentorise.game.managers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.RabbitConf;
import eu.trentorise.game.notification.LevelGainedNotification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class, RabbitConf.class, BraveAutoConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class RabbitMQTest {

	private static final String PLAYER_ID = "8";

	private static final String GAME_ID = "5b7a885149c95d50c5f9d442";

	@Autowired
	private NotificationManager notificationManager;

	@Autowired
	private MongoTemplate mongo;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.getDb().drop();
	}

	@Test
	public void messageNotification() throws Exception {
		LevelGainedNotification lgn = new LevelGainedNotification();
		lgn.setGameId(GAME_ID);
		lgn.setPlayerId(PLAYER_ID);

		notificationManager.notificate(lgn);
	}

}
