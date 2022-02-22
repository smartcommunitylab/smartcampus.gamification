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

package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.Level.Config;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.repo.ChallengeConceptPersistence;
import eu.trentorise.game.repo.GenericObjectPersistence;
import eu.trentorise.game.repo.StatePersistence;

public class PlayerState {

    private final Logger logger = LoggerFactory.getLogger(PlayerState.class);

    private String playerId;
    private String gameId;

    private List<PlayerLevel> levels = new ArrayList<>();
    private Set<GameConcept> state = new HashSet<GameConcept>();

    private CustomData customData = new CustomData();

    private Inventory inventory = new Inventory();

    public PlayerState() {}

    public PlayerState(String gameId, String playerId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public PlayerState(StatePersistence statePersistence) {
        if (statePersistence != null) {
            ObjectMapper mapper = new ObjectMapper();
            gameId = statePersistence.getGameId();
            playerId = statePersistence.getPlayerId();
            customData = statePersistence.getCustomData();

            // FIXME useless
            state = new HashSet<GameConcept>();

            for (Map<String, GenericObjectPersistence> map : statePersistence.getConcepts()
                    .values()) {
                for (GenericObjectPersistence obj : map.values()) {
                    try {
                        state.add(mapper.convertValue(obj.getObj(),
                                (Class<? extends GameConcept>) Thread.currentThread()
                                        .getContextClassLoader().loadClass(obj.getType())));
                    } catch (Exception e) {
                        LogHub.error(statePersistence.getGameId(), logger,
                                "Problem to load class {}", obj.getType(), e);
                    }
                }
            }

            levels.addAll(statePersistence.getLevels());
            inventory = statePersistence.getInventory();
        }
    }

    public PlayerState clone() {
        PlayerState cloned = new PlayerState(gameId, playerId);
        cloned.setState(new HashSet<GameConcept>());
        for (GameConcept gc : state) {
            if (gc instanceof PointConcept) {
                PointConcept pointCopy = new PointConcept(gc.getName());
                pointCopy.setScore(((PointConcept) gc).getScore());
                cloned.getState().add(pointCopy);
            } else if (gc instanceof BadgeCollectionConcept) {
                BadgeCollectionConcept collectionCopy = new BadgeCollectionConcept(gc.getName());
                collectionCopy.getBadgeEarned()
                        .addAll(((BadgeCollectionConcept) gc).getBadgeEarned());
                cloned.getState().add(collectionCopy);
            }
        }

        return cloned;
    }


    public PointConcept pointConcept(String name) {
        return state.stream().filter(
                concept -> concept instanceof PointConcept && concept.getName().equals(name))
                .map(concept -> (PointConcept) concept).findFirst().orElse(null);
    }

    public List<PointConcept> pointConcepts() {
        return state.stream().filter(concept -> concept instanceof PointConcept)
                .map(concept -> (PointConcept) concept).collect(Collectors.toList());
    }

    public List<ChallengeConcept> challenges() {
        return state.stream().filter(concept -> concept instanceof ChallengeConcept)
                .map(concept -> (ChallengeConcept) concept)
                .collect(Collectors.toList());
    }

    public <T extends GameConcept> T removeConcept(String conceptName, Class<T> classType) {
        Iterator<GameConcept> stateIterator = state.iterator();
        T removed = null;
        while (stateIterator.hasNext()) {
            GameConcept gc = stateIterator.next();
            if (gc.getClass() == classType && gc.getName().equals(conceptName)) {
                state.remove(gc);
                removed = (T) gc;
                break;
            }
        }

        return removed;
    }


    /**
     * Clear and update levels for the player
     * 
     * @param newLevelState
     * @return
     */
    public PlayerState updateLevels(List<PlayerLevel> newLevelState) {
        levels.clear();
        if (levels != null) {
            levels.addAll(newLevelState);
        }
        return this;
    }

    public Set<GameConcept> getState() {
        return state;
    }

    public void setState(Set<GameConcept> state) {
        this.state = state;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public CustomData getCustomData() {
        return customData;
    }

    public void setCustomData(CustomData customData) {
        this.customData = customData;
    }

    public List<PlayerLevel> getLevels() {
        return levels;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PlayerState updateInventory(Game game, List<LevelInstance> newLevels) {
        if (game != null) {
            if (!game.getId().equals(gameId)) {
                throw new IllegalArgumentException(
                        String.format("State of player not belong to game %s", game.getId()));
            }

            if (newLevels == null) {
                newLevels = new ArrayList<>();
            }

            Map<LevelInstance, Config> configs = gameLevelConfigs(game);
            newLevels.forEach(levelInstance -> {
                Config levelConfig = configs.get(levelInstance);
                if (levelConfig != null) {
                    inventory.upgrade(levelConfig);
                    LogHub.info(game.getId(), logger,
                            String.format(
                                    "Upgrade Inventory: available items %s, active items %s, added %s activationActions",
                                    levelConfig.getAvailableModels(), levelConfig.getActiveModels(),
                                    levelConfig.getChoices()));
                }
            });
        }
        return this;
    }

    /**
     * Try to remove a challenge identified by a instanceName from the player state.
     * 
     * @param instanceName id of the challenge
     * @return Optional value: empty if challenge doesn't exist in PlayerState
     */
    public Optional<ChallengeConcept> removeChallenge(String instanceName) {
        return Optional.ofNullable(removeConcept(instanceName, ChallengeConcept.class));
    }

    private Map<LevelInstance, Config> gameLevelConfigs(Game game) {
        Map<LevelInstance, Config> configs = new HashMap<>();
        game.getLevels().forEach(level -> {
            configs.putAll(levelConfigs(level));
        });
        return configs;
    }

    private Map<LevelInstance, Config> levelConfigs(Level level) {
        Map<LevelInstance, Config> configs = new HashMap<>();
        level.getThresholds().forEach(threshold -> {
            if (threshold.getConfig() != null) {
                configs.put(new LevelInstance(level.getName(), threshold.getName()),
                        threshold.getConfig());
            }
        });

        return configs;
    }

    public Optional<ChallengeConcept> challenge(String name) {
        return challenges().stream().filter(c -> c.getName().equals(name)).findFirst();
    }

    public PlayerState upateChallenge(ChallengeUpdate changes) {
        for (GameConcept concept : state) {
            if (concept.getClass() == ChallengeConcept.class) {
                ChallengeConcept challenge = (ChallengeConcept) concept;
                if (challenge.getName().equals(changes.getName())) {
                    challenge = challenge.update(changes);
                    break;
                }
            }
        }
        return this;
    }
    
    public void loadChallengeConcepts(List<ChallengeConceptPersistence> listCcps) {
    	listCcps.forEach(ccp -> {
        	 state.add(ccp.getConcept());
        });
    }
    
}
