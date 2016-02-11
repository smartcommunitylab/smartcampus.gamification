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
import java.util.HashSet;
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
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.repo.PlayerRepo;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.repo.TeamPersistence;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@Component
public class DBPlayerManager implements PlayerService {

	private final Logger logger = LoggerFactory
			.getLogger(DBPlayerManager.class);

	@Autowired
	private PlayerRepo playerRepo;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GameService gameSrv;

	public PlayerState loadState(String gameId, String playerId, boolean upsert) {
		eu.trentorise.game.repo.StatePersistence state = playerRepo
				.findByGameIdAndPlayerId(gameId, playerId);
		PlayerState res = state == null ? (upsert ? new PlayerState(playerId,
				gameId) : null) : state.toPlayerState().isTeam() ? new Team(
				state) : state.toPlayerState();
		return updateConcepts(res, gameId);
	}

	public boolean saveState(PlayerState state) {
		StatePersistence toSave = null;
		if (state instanceof Team) {
			toSave = new TeamPersistence((Team) state);
		} else {
			toSave = new StatePersistence(state);
		}
		persist(toSave);
		return true;
	}

	private StatePersistence persist(StatePersistence state) {
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
		update.set("concepts", state.getConcepts());
		update.set("customData", state.getCustomData());
		update.set("metadata", state.getMetadata());
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.upsert(true);
		options.returnNew(true);
		return mongoTemplate.findAndModify(query, update, options,
				StatePersistence.class);
	}

	public List<String> readPlayers(String gameId) {
		List<StatePersistence> states = playerRepo.findByGameId(gameId);
		List<String> result = new ArrayList<String>();
		for (StatePersistence state : states) {
			result.add(state.getPlayerId());
		}

		return result;
	}

	@Override
	public Page<String> readPlayers(String gameId, Pageable pageable) {
		Page<StatePersistence> states = playerRepo.findByGameId(gameId,
				pageable);
		List<String> result = new ArrayList<String>();
		for (StatePersistence state : states) {
			result.add(state.getPlayerId());
		}
		PageImpl<String> res = new PageImpl<String>(result, pageable,
				states.getTotalElements());
		return res;
	}

	public Page<PlayerState> loadStates(String gameId, Pageable pageable) {
		Page<StatePersistence> states = playerRepo.findByGameId(gameId,
				pageable);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(updateConcepts(state.toPlayerState(), gameId));
		}
		PageImpl<PlayerState> res = new PageImpl<PlayerState>(result, pageable,
				states.getTotalElements());
		return res;
	}

	@Override
	public List<PlayerState> loadStates(String gameId) {
		List<StatePersistence> states = playerRepo.findByGameId(gameId);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(updateConcepts(state.toPlayerState(), gameId));
		}

		return result;
	}

	@Override
	public Page<PlayerState> loadStates(String gameId, String playerId,
			Pageable pageable) {
		Page<StatePersistence> states = playerRepo.findByGameIdAndPlayerIdLike(
				gameId, playerId, pageable);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(updateConcepts(state.toPlayerState(), gameId));
		}
		PageImpl<PlayerState> res = new PageImpl<PlayerState>(result, pageable,
				states.getTotalElements());
		return res;
	}

	@Override
	public List<PlayerState> loadStates(String gameId, String playerId) {
		List<StatePersistence> states = playerRepo.findByGameIdAndPlayerIdLike(
				gameId, playerId);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(updateConcepts(state.toPlayerState(), gameId));
		}

		return result;
	}

	private PlayerState updateConcepts(PlayerState ps, String gameId) {
		if (ps != null) {
			Game g = gameSrv.loadGameDefinitionById(gameId);
			if (ps.getState() == null) {
				ps.setState(new HashSet<GameConcept>());
			}
			if (g != null) {
				List<GameConcept> toAppend = new ArrayList<GameConcept>();
				if (g.getConcepts() != null) {
					for (GameConcept gc : g.getConcepts()) {
						boolean found = false;
						for (GameConcept pgc : ps.getState()) {
							found = gc.getName().equals(pgc.getName())
									&& gc.getClass().equals(pgc.getClass());
							if (found) {
								break;
							}
						}
						if (!found) {
							toAppend.add(gc);
						}
					}
				}
				ps.getState().addAll(toAppend);
			}
		}
		return ps;
	}

	@Override
	public Team saveTeam(Team team) {
		TeamPersistence tp = new TeamPersistence(team);
		StatePersistence saved = persist(tp);
		return new Team(saved);
	}

	@Override
	public List<Team> readTeams(String gameId) {
		List<StatePersistence> result = playerRepo.findTeamsByGameId(gameId);
		List<Team> converted = new ArrayList<>();
		for (StatePersistence sp : result) {
			converted.add(new Team(sp));
		}

		return converted;
	}

	@Override
	public void deleteState(String gameId, String playerId) {
		playerRepo.deleteByGameIdAndPlayerId(gameId, playerId);
	}

	@Override
	public List<Team> readTeams(String gameId, String playerId) {
		List<StatePersistence> result = playerRepo.findTeamByMemberId(gameId,
				playerId);
		List<Team> converted = new ArrayList<>();
		for (StatePersistence sp : result) {
			converted.add(new Team(sp));
		}

		return converted;
	}

	@Override
	public Team addToTeam(String gameId, String teamId, String playerId) {
		StatePersistence state = playerRepo.findByGameIdAndPlayerId(gameId,
				teamId);
		if (state != null) {
			List<String> members = (List<String>) state.getMetadata().get(
					"members");
			if (members != null) {
				members.add(playerId);
				state.getMetadata().put("members", members);
				playerRepo.save(state);
			}
		}

		return new Team(state);
	}

	@Override
	public Team removeFromTeam(String gameId, String teamId, String playerId) {
		StatePersistence state = playerRepo.findByGameIdAndPlayerId(gameId,
				teamId);
		if (state != null) {
			List<String> members = (List<String>) state.getMetadata().get(
					"members");
			if (members != null) {
				members.remove(playerId);
				state.getMetadata().put("members", members);
				playerRepo.save(state);
			}
		}

		return new Team(state);
	}

	@Override
	public Team readTeam(String gameId, String teamId) {
		StatePersistence state = playerRepo.findByGameIdAndPlayerId(gameId,
				teamId);
		if (state != null) {
			return (Team) updateConcepts(new Team(state), gameId);
		}
		return null;
	}
}
