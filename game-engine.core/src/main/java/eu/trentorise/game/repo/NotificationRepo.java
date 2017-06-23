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

package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends CrudRepository<NotificationPersistence, String> {

	@Query(value = "{'obj.gameId' : ?0}")
	public List<NotificationPersistence> findByGameId(String gameId);

	@Query(value = "{'obj.gameId' : ?0}")
	public List<NotificationPersistence> findByGameId(String gameId, Pageable pageable);

	@Query(value = "{'obj.gameId' : ?0, 'obj.timestamp':{'$gte' : ?1}}")
	public List<NotificationPersistence> findByGameIdAndTimestampGreaterThan(String gameId, long timestamp);

	@Query(value = "{'obj.gameId' : ?0, 'obj.timestamp':{'$gte' : ?1}}")
	public List<NotificationPersistence> findByGameIdAndTimestampGreaterThan(String gameId, long timestamp,
			Pageable pageable);

	@Query(value = "{'obj.gameId' : ?0, 'obj.timestamp':{'$lt' : ?1}}")
	public List<NotificationPersistence> findByGameIdAndTimestampLessThan(String gameId, long timestamp);

	@Query(value = "{'obj.gameId' : ?0, 'obj.timestamp':{'$lt' : ?1}}")
	public List<NotificationPersistence> findByGameIdAndTimestampLessThan(String gameId, long timestamp,
			Pageable pageable);

	@Query(value = "{'obj.gameId' : ?0, 'obj.timestamp':{'$gte' : ?1, '$lt': ?2}}")
	public List<NotificationPersistence> findByGameIdAndTimestampBetween(String gameId, long fromTs, long toTs);

	@Query(value = "{'obj.gameId' : ?0, 'obj.timestamp':{'$gte' : ?1, '$lt': ?2}}")
	public List<NotificationPersistence> findByGameIdAndTimestampBetween(String gameId, long fromTs, long toTs,
			Pageable pageable);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1}")
	public List<NotificationPersistence> findByGameIdAndPlayerId(String gameId, String playerId);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1}")
	public List<NotificationPersistence> findByGameIdAndPlayerId(String gameId, String playerId, Pageable pageable);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1, 'obj.timestamp':{'$gte' : ?2}}")
	public List<NotificationPersistence> findByGameIdAndPlayerIdAndTimestampGreaterThan(String gameId, String playerId,
			long timestamp);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1, 'obj.timestamp':{'$gte' : ?2}}")
	public List<NotificationPersistence> findByGameIdAndPlayerIdAndTimestampGreaterThan(String gameId, String playerId,
			long timestamp, Pageable pageable);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1, 'obj.timestamp':{'$lt' : ?2}}")
	public List<NotificationPersistence> findByGameIdAndPlayerIdAndTimestampLessThan(String gameId, String playerId,
			long timestamp);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1, 'obj.timestamp':{'$lt' : ?2}}")
	public List<NotificationPersistence> findByGameIdAndPlayerIdAndTimestampLessThan(String gameId, String playerId,
			long timestamp, Pageable pageable);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1, 'obj.timestamp':{'$gte' : ?2, '$lt': ?3}}")
	public List<NotificationPersistence> findByGameIdAndPlayerIdAndTimestampBetween(String gameId, String playerId,
			long fromTs, long toTs);

	@Query(value = "{'obj.gameId' : ?0, 'obj.playerId': ?1, 'obj.timestamp':{'$gte' : ?2, '$lt': ?3}}")
	public List<NotificationPersistence> findByGameIdAndPlayerIdAndTimestampBetween(String gameId, String playerId,
			long fromTs, long toTs, Pageable pageable);
}
