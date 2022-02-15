package eu.trentorise.game.managers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.notification.LevelGainedNotification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class RabbitMQTest {

	private static final String PLAYER_ID = "8";

	private static final String GAME_ID = "coreGameTest";
	private static final String QUEUE_ID = "queue-" + GAME_ID;

	@Autowired
	private NotificationManager notificationManager;

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private RabbitMQManager rabbitMQManager;

	@Before
	public void cleanDB() {
		init();
		// clean mongo
		mongo.getDb().drop();
		// clean test queue.
		try {
			rabbitMQManager.init();
			rabbitMQManager.drop(QUEUE_ID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void destroy() throws IOException, TimeoutException {
		System.setProperty("rabbitmq.enabled", "false");
		rabbitMQManager.drop(QUEUE_ID);
		rabbitMQManager.setInitialized(false);
		rabbitMQManager.close();		
	}

	public void init() {
		System.setProperty("rabbitmq.enabled", "true");
		System.setProperty("rabbitmq.host", "localhost");
		System.setProperty("rabbitmq.virtualhost", "playgo");
		System.setProperty("rabbitmq.port", "5672");
		System.setProperty("rabbitmq.pngExchangeName", "ge-notifications");
		System.setProperty("rabbitmq.pngRoutingKeyPrefix", "game");
		System.setProperty("rabbitmq.user", "user");
		System.setProperty("rabbitmq.password", "password");
	}

	@Test
	public void messageNotification() throws Exception {
		LevelGainedNotification lgn = new LevelGainedNotification();
		lgn.setGameId(GAME_ID);
		lgn.setPlayerId(PLAYER_ID);
		notificationManager.notificate(lgn);
		Thread.sleep(2000);
		assertThat(
				rabbitMQManager.getRabbitMQChannel().queueDeclare(QUEUE_ID, true, false, false, null).getMessageCount(),
				is(1));
	}
}
