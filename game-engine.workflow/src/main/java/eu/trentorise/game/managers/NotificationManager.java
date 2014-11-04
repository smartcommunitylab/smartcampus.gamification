package eu.trentorise.game.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.Notification;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.NotificationRepo;

@Component
public class NotificationManager {

	@Autowired
	NotificationRepo repo;

	public void notificate(Notification n) {
		repo.save(new NotificationPersistence(n));
	}
}
