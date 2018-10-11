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

package eu.trentorise.game.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.ClassificationBoard;
import eu.trentorise.game.model.core.ComplexSearchQuery;
import eu.trentorise.game.model.core.RawSearchQuery;
import eu.trentorise.game.model.core.StringSearchQuery;

@Service
public interface PlayerService {

	public PlayerState loadState(String gameId, String playerId, boolean upsert, boolean mergeGroupChallenges);

	public Page<PlayerState> loadStates(String gameId, Pageable pageable, boolean mergeGroupChallenges);

	public List<PlayerState> loadStates(String gameId);

	public Page<PlayerState> loadStates(String gameId, String playerId, Pageable pageable, boolean mergeGroupChallenges);

	public ClassificationBoard classifyAllPlayerStates(Game g, String itemType, Pageable pageable);

	public ClassificationBoard classifyPlayerStatesWithKey(long timestamp, String pointConceptName, String periodName, String key, String gameId, Pageable pageable);

	public PlayerState saveState(PlayerState state);

	public PlayerState updateCustomData(String gameId, String playerId, Map<String, Object> data);

	public void deleteState(String gameId, String playerId);

	public Page<String> readPlayers(String gameId, Pageable pageable);

	public List<String> readPlayers(String gameId);

	public Page<PlayerState> search(String gameId, RawSearchQuery query, Pageable pageable);

	public Page<PlayerState> search(String gameId, ComplexSearchQuery query, Pageable pageable);

	public Page<PlayerState> search(String gameId, StringSearchQuery query, Pageable pageable);

	/*
	 * TEAM METHODS
	 */

	public TeamState saveTeam(TeamState team);

	public List<TeamState> readTeams(String gameId);

	public TeamState readTeam(String gameId, String teamId);

	public List<TeamState> readTeams(String gameId, String playerId);

	public TeamState addToTeam(String gameId, String teamId, String playerId);

	public TeamState removeFromTeam(String gameId, String teamId, String playerId);

	/*
	 * CHALLENGE METHODS
	 */

    public ChallengeConcept assignChallenge(String gameId, String playerId, ChallengeAssignment challengeAssignment);

    public ChallengeConcept acceptChallenge(String gameId, String playerId, String challengeName);

    public ChallengeConcept forceChallengeChoice(String gameId, String playerId);
}
