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
