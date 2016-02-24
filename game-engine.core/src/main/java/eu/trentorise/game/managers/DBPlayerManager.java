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
import java.util.Map;

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

import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.repo.GenericObjectPersistence;
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
		PlayerState res = state == null ? (upsert ? new PlayerState(gameId,
				playerId) : null) : isTeam(state) ? new Team(state)
				: new PlayerState(state);
		return initConceptsStructure(res, gameId);
	}

	public PlayerState saveState(PlayerState state) {
		PlayerState saved = null;
		if (state != null) {
			StatePersistence toSave = null;
			if (state instanceof Team) {
				toSave = new TeamPersistence((Team) state);
				saved = new Team(persist(toSave));
			} else {
				toSave = new StatePersistence(state);
				saved = new PlayerState(persist(toSave));
			}
		}
		return saved;
	}

	private boolean isTeam(StatePersistence state) {
		return state != null
				&& state.getMetadata().get(Team.NAME_METADATA) != null
				&& state.getMetadata().get(Team.MEMBERS_METADATA) != null;
	}

	private StatePersistence persist(StatePersistence state) {
		return persist(state.getGameId(), state.getPlayerId(),
				state.getConcepts(), state.getCustomData(), state.getMetadata());
	}

	private StatePersistence persistConcepts(String gameId, String playerId,
			List<GenericObjectPersistence> concepts) {
		return persist(gameId, playerId, concepts, null, null);
	}

	private StatePersistence persistCustomData(String gameId, String playerId,
			CustomData data) {
		return persist(gameId, playerId, null, data, null);
	}

	private StatePersistence persistCustomData(String gameId, String playerId,
			Map<String, Object> customData) {
		CustomData c = new CustomData();
		c.putAll(customData);
		return persist(gameId, playerId, null, c, null);
	}

	private StatePersistence persistMetadata(String gameId, String playerId,
			Map<String, Object> metadata) {
		return persist(gameId, playerId, null, null, metadata);
	}

	private StatePersistence persist(String gameId, String playerId,
			List<GenericObjectPersistence> concepts, CustomData customData,
			Map<String, Object> metadata) {
		if (StringUtils.isBlank(gameId) || StringUtils.isBlank(playerId)) {
			throw new IllegalArgumentException(
					"field gameId and playerId of PlayerState MUST be set");
		}

		Criteria criteria = new Criteria();
		criteria = criteria.and("gameId").is(gameId).and("playerId")
				.is(playerId);
		Query query = new Query(criteria);
		Update update = new Update();
		if (concepts != null) {
			update.set("concepts", concepts);
		}
		if (customData != null) {
			update.set("customData", customData);
		}
		if (metadata != null) {
			update.set("metadata", metadata);
		}
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
			result.add(initConceptsStructure(new PlayerState(state), gameId));
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
			result.add(initConceptsStructure(new PlayerState(state), gameId));
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
			result.add(initConceptsStructure(new PlayerState(state), gameId));
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
			result.add(initConceptsStructure(new PlayerState(state), gameId));
		}

		return result;
	}

	private PlayerState initConceptsStructure(PlayerState ps, String gameId) {
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
		return (Team) saveState(team);
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
					Team.MEMBERS_METADATA);
			if (members != null) {
				members.add(playerId);
				state.getMetadata().put(Team.MEMBERS_METADATA, members);
				playerRepo.save(state);
			}
			return new Team(state);
		}

		return null;

	}

	@Override
	public Team removeFromTeam(String gameId, String teamId, String playerId) {
		StatePersistence state = playerRepo.findByGameIdAndPlayerId(gameId,
				teamId);
		if (state != null) {
			List<String> members = (List<String>) state.getMetadata().get(
					Team.MEMBERS_METADATA);
			if (members != null) {
				members.remove(playerId);
				state.getMetadata().put(Team.MEMBERS_METADATA, members);
				playerRepo.save(state);
			}
		}

		return new Team(state);
	}

	@Override
	public Team readTeam(String gameId, String teamId) {
		return (Team) loadState(gameId, teamId, false);
	}

	@Override
	public PlayerState updateCustomData(String gameId, String playerId,
			Map<String, Object> data) {
		// findAndModify only customdata to avoid concurrent accesses on same
		// data
		StatePersistence state = persistCustomData(gameId, playerId, data);
		return new PlayerState(state);

	}
}
