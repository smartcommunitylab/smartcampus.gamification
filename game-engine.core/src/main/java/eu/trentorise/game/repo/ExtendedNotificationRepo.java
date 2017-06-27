package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ExtendedNotificationRepo {

	public List<NotificationPersistence> findGameNotificationsByQuery(String gameId, NotificationQuery query,
			Pageable pageable);

	public List<NotificationPersistence> findPlayerNotificationsByQuery(String gameId, String playerId,
			NotificationQuery query, Pageable pageable);
}
