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

package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.repo.PlayerRepo;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@Component
public class DBPlayerManager implements PlayerService {

	private final Logger logger = LoggerFactory
			.getLogger(DBPlayerManager.class);

	@Autowired
	private PlayerRepo repo;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GameService gameSrv;

	public PlayerState loadState(String userId, String gameId) {
		eu.trentorise.game.repo.StatePersistence state = repo
				.findByGameIdAndPlayerId(gameId, userId);

		return state == null ? init(userId, gameId) : state.toPlayerState();
	}

	public boolean saveState(PlayerState state) {
		StatePersistence toSave = new StatePersistence(state);

		if (StringUtils.isBlank(state.getGameId())
				|| StringUtils.isBlank(state.getPlayerId())) {
			throw new IllegalArgumentException(
					"field gameId and playerId of PlayerState MUST be set");
		}

		Criteria criteria = new Criteria();
		criteria = criteria.and("gameId").is(state.getGameId()).and("playerId")
				.is(state.getPlayerId());
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("concepts", toSave.getConcepts());
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.upsert(true);
		mongoTemplate.findAndModify(query, update, options,
				StatePersistence.class);
		return true;
	}

	public List<String> readPlayers(String gameId) {
		List<StatePersistence> states = repo.findByGameId(gameId);
		List<String> result = new ArrayList<String>();
		for (StatePersistence state : states) {
			result.add(state.getPlayerId());
		}

		return result;
	}

	@Override
	public Page<String> readPlayers(String gameId, Pageable pageable) {
		Page<StatePersistence> states = repo.findByGameId(gameId, pageable);
		List<String> result = new ArrayList<String>();
		for (StatePersistence state : states) {
			result.add(state.getPlayerId());
		}
		PageImpl<String> res = new PageImpl<String>(result, pageable,
				states.getTotalElements());
		return res;
	}

	public Page<PlayerState> loadStates(String gameId, Pageable pageable) {
		Page<StatePersistence> states = repo.findByGameId(gameId, pageable);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(state.toPlayerState());
		}
		PageImpl<PlayerState> res = new PageImpl<PlayerState>(result, pageable,
				states.getTotalElements());
		return res;
	}

	@Override
	public List<PlayerState> loadStates(String gameId) {
		List<StatePersistence> states = repo.findByGameId(gameId);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(state.toPlayerState());
		}

		return result;
	}

	@Override
	public Page<PlayerState> loadStates(String gameId, String userId,
			Pageable pageable) {
		Page<StatePersistence> states = repo.findByGameIdAndPlayerIdLike(
				gameId, userId, pageable);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(state.toPlayerState());
		}
		PageImpl<PlayerState> res = new PageImpl<PlayerState>(result, pageable,
				states.getTotalElements());
		return res;
	}

	@Override
	public List<PlayerState> loadStates(String gameId, String userId) {
		List<StatePersistence> states = repo.findByGameIdAndPlayerIdLike(
				gameId, userId);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(state.toPlayerState());
		}

		return result;
	}

	private PlayerState init(String playerId, String gameId) {
		Game g = gameSrv.loadGameDefinitionById(gameId);
		PlayerState p = new PlayerState(playerId, gameId);
		if (g != null) {
			p.setState(g.getConcepts());
		}

		return p;
	}

}
