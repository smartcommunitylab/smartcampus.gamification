package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.List;

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

	public List<Notification> readNotifications(String gameId) {
		List<NotificationPersistence> nots = repo.findByGameId(gameId);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, long timestamp) {
		List<NotificationPersistence> nots = repo
				.findByGameIdAndTimestampGreaterThan(gameId, timestamp);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId) {
		List<NotificationPersistence> nots = repo.findByGameIdAndPlayerId(
				gameId, playerId);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId,
			long timestamp) {
		List<NotificationPersistence> nots = repo
				.findByGameIdAndPlayerIdAndTimestampGreaterThan(gameId,
						playerId, timestamp);
		return convert(nots);
	}

	private List<Notification> convert(
			List<NotificationPersistence> notifications) {
		List<Notification> result = new ArrayList<Notification>();
		for (NotificationPersistence not : notifications) {
			Notification n = not.toNotification();
			if (n != null) {
				result.add(n);
			}
		}
		return result;

	}
}
