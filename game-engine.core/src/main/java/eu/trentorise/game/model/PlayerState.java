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

package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.repo.GenericObjectPersistence;
import eu.trentorise.game.repo.StatePersistence;

public class PlayerState {

	private final Logger logger = LoggerFactory.getLogger(PlayerState.class);

	private String playerId;
	private String gameId;

    private List<PlayerLevel> levels = new ArrayList<>();
	private Set<GameConcept> state = new HashSet<GameConcept>();

	private CustomData customData = new CustomData();

	public PlayerState() {
	}

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

			for (Map<String, GenericObjectPersistence> map : statePersistence.getConcepts().values()) {
				for (GenericObjectPersistence obj : map.values()) {
					try {
						state.add(mapper.convertValue(obj.getObj(), (Class<? extends GameConcept>) Thread
								.currentThread().getContextClassLoader().loadClass(obj.getType())));
					} catch (Exception e) {
						LogHub.error(statePersistence.getGameId(), logger, "Problem to load class {}", obj.getType(),
								e);
					}
				}
			}

            levels.addAll(statePersistence.getLevels());
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
				collectionCopy.getBadgeEarned().addAll(((BadgeCollectionConcept) gc).getBadgeEarned());
				cloned.getState().add(collectionCopy);
			}
		}

		return cloned;
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

}