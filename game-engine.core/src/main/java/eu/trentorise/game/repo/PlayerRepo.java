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
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends
		PagingAndSortingRepository<StatePersistence, String> {

	public List<StatePersistence> findByGameId(String id);

	public Page<StatePersistence> findByGameId(String id, Pageable pageable);

	public List<StatePersistence> findByPlayerId(String id);

	public Page<StatePersistence> findByPlayerId(String id, Pageable pageable);

	public StatePersistence findByGameIdAndPlayerId(String game, String player);

	public List<StatePersistence> findByGameIdAndPlayerIdLike(String id,
			String player);

	public Page<StatePersistence> findByGameIdAndPlayerIdLike(String id,
			String player, Pageable pageable);

}
