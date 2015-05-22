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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;

@Document(collection = "playerState")
public class StatePersistence {

	@Transient
	private final Logger logger = LoggerFactory
			.getLogger(StatePersistence.class);
	@Id
	private String id;

	private String playerId;
	private String gameId;

	private List<GenericObjectPersistence> concepts = new ArrayList<GenericObjectPersistence>();

	private CustomData customData;

	public StatePersistence(PlayerState state) {
		playerId = state.getPlayerId();
		gameId = state.getGameId();

		for (GameConcept gc : state.getState()) {
			concepts.add(new GenericObjectPersistence(gc));
		}

		customData = state.getCustomData();
	}

	public StatePersistence() {
	}

	public PlayerState toPlayerState() {
		ObjectMapper mapper = new ObjectMapper();
		PlayerState p = new PlayerState();
		p.setGameId(gameId);
		p.setPlayerId(playerId);
		p.setCustomData(customData);
		Set<GameConcept> state = new HashSet<GameConcept>();
		for (GenericObjectPersistence obj : concepts) {
			try {
				state.add(mapper.convertValue(
						obj.getObj(),
						(Class<? extends GameConcept>) Thread.currentThread()
								.getContextClassLoader()
								.loadClass(obj.getType())));
			} catch (Exception e) {
				logger.error("Problem to load class {}", obj.getType());
			}
		}

		p.setState(state);

		return p;
	}

	public List<GenericObjectPersistence> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<GenericObjectPersistence> concepts) {
		this.concepts = concepts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
