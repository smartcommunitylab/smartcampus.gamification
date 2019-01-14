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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends
		PagingAndSortingRepository<StatePersistence, String>, ExtendPlayerRepo {

	public List<StatePersistence> findByGameId(String gameId);

	public Page<StatePersistence> findByGameId(String gameId, Pageable pageable);

	public List<StatePersistence> findByPlayerId(String playerId);

	public Page<StatePersistence> findByPlayerId(String playerId,
			Pageable pageable);

	public StatePersistence findByGameIdAndPlayerId(String gameId,
			String playerId);

	public List<StatePersistence> findByGameIdAndPlayerIdLike(String gameId,
			String playerId);

	public Page<StatePersistence> findByGameIdAndPlayerIdLike(String gameId,
			String playerId, Pageable pageable);

	@Query("{'gameId':?0, 'metadata.team-name':{$exists:true},'metadata.team-members':{$exists:true}}")
	public List<StatePersistence> findTeamsByGameId(String gameId);

	public List<StatePersistence> deleteByGameIdAndPlayerId(String gameId,
			String playerId);

	@Query("{'gameId':?0, 'metadata.team-members':?1}")
	public List<StatePersistence> findTeamByMemberId(String gameId,
			String memberId);

}
