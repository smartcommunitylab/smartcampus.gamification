package eu.trentorise.game.managers;

import java.util.List;

import org.junit.Assert;
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
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.notification.MessageNotification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class NotificationManagerTest {

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
	public void messageNotification() {
		MessageNotification message = new MessageNotification("TEST", "1000",
				"my_key");
		message.addData("payload", "I\'m the payload");
		notificationManager.notificate(message);

		List<Notification> notifications = notificationManager
				.readNotifications("TEST");
		Assert.assertEquals(1, notifications.size());
	}

}
