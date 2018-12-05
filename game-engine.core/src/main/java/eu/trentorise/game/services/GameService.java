/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.trentorise.game.services;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameStatistics;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.Rule;

@Service
public interface GameService {

    public String getGameIdByAction(String actionId);

    public void startupTasks(String gameId);

    public Game saveGameDefinition(Game game);

    public Game loadGameDefinitionById(String gameId);

    public Game loadGameDefinitionByAction(String actionId);

    public boolean deleteGame(String gameId);

    public List<Game> loadAllGames();

    public List<Game> loadGames(boolean onlyActive);

    public List<Game> loadGameByOwner(String user);

    public List<Game> loadGameByOwner(String domain, String user);

    public List<Game> loadGameByDomain(String domain);

    public String addRule(Rule rule);

    public Rule loadRule(String gameId, String url);

    public boolean deleteRule(String gameId, String url);

    public void addConceptInstance(String gameId, GameConcept gc);

    public Set<GameConcept> readConceptInstances(String gameId);

    public ChallengeModel saveChallengeModel(String gameId, ChallengeModel model);

    public boolean deleteChallengeModel(String gameId, String modelId);

    public Set<ChallengeModel> readChallengeModels(String gameId);

    public ChallengeModel readChallengeModel(String gameId, String modelId);

    Game upsertLevel(String gameId, Level level);

    Game deleteLevel(String gameId, String levelName);

    Level addLevelThreshold(String gameId, String levelName, Threshold threshold);

    Level deleteLevelThreshold(String gameId, String levelName, String thresholdName);

    Level updateLevelThreshold(String gameId, String levelName, String thresholdName,
            double thresholdValue);

    List<PlayerLevel> calculateLevels(String gameId, PlayerState playerState);

    void conditionCheckPerformanceGroupChallengesTask();
    
    void taskGameStats();
    
    void challengeFailureTask();

	public List<GameStatistics> loadGameStats(String gameId, String pointConceptName, String periodName, Long timestamp,
			String periodIndex, Pageable pageable);

}
