package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends
		CrudRepository<NotificationPersistence, String> {

	@Query(value = "{'obj.gameId' : ?0}")
	public List<NotificationPersistence> findByGameId(String gameId);

	@Query(value = "{'obj.gameId' : ?0, 'obj.timestamp':{'$gt' : ?1}}")
	public List<NotificationPersistence> findByGameIdAndTimestampGreaterThan(
			String gameId, long timestamp);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1}")
	public List<NotificationPersistence> findByGameIdAndPlayerId(String gameId,
			String playerId);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1, 'obj.timestamp':{'$gt' : ?2}}")
	public List<NotificationPersistence> findByGameIdAndPlayerIdAndTimestampGreaterThan(
			String gameId, String playerId, long timestamp);
}
